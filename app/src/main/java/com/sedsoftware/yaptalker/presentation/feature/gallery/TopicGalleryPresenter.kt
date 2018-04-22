package com.sedsoftware.yaptalker.presentation.feature.gallery

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.ImageHelperInteractor
import com.sedsoftware.yaptalker.domain.interactor.TopicGalleryInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.feature.gallery.adapter.TopicGalleryLoadMoreClickListener
import com.sedsoftware.yaptalker.presentation.feature.topic.GalleryInitialState
import com.sedsoftware.yaptalker.presentation.mapper.TopicGalleryModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostGalleryImageModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class TopicGalleryPresenter @Inject constructor(
  settings: Settings,
  private val topicGalleryInteractor: TopicGalleryInteractor,
  private val imageHelperInteractor: ImageHelperInteractor,
  private val galleryMapper: TopicGalleryModelMapper,
  private val initialState: GalleryInitialState
) : BasePresenter<TopicGalleryView>(), TopicGalleryLoadMoreClickListener {

  companion object {
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private val postsPerPage = settings.getMessagesPerPage()
  private var currentPage = 1
  private var totalPages = 1
  private var currentImage = ""
  private var currentTitleLabel = ""

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    loadTopicGallery()
  }

  override fun onLoadMoreClicked() {
    currentPage++
    loadTopicCurrentPageGallery()
  }

  private fun loadTopicGallery() {
    currentPage = initialState.currentPage
    currentImage = initialState.currentImage

    loadTopicCurrentPageGallery()
  }

  private fun loadTopicCurrentPageGallery() {

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage

    topicGalleryInteractor
      .getTopicGallery(initialState.currentForumId, initialState.currentTopicId, startingPost)
      .subscribeOn(Schedulers.io())
      .map(galleryMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getTopicGalleryObserver())
  }

  private fun getTopicGalleryObserver() =
    object : DisposableSingleObserver<List<YapEntity>>() {

      override fun onSuccess(items: List<YapEntity>) {

        items.forEach { item ->
          when (item) {
            is NavigationPanelModel -> {
              currentPage = item.currentPage
              totalPages = item.totalPages
              currentTitleLabel = item.navigationLabel
            }
          }
        }

        if (currentPage == totalPages) {
          viewState.lastPageReached()
        }

        val images = items.filter { it is SinglePostGalleryImageModel }
        viewState.appendImages(images)
        viewState.updateCurrentUiState(currentTitleLabel)

        if (currentImage.isNotEmpty()) {
          viewState.scrollToSelectedImage(currentImage)
          currentImage = ""
        } else if (images.isNotEmpty()) {
          viewState.scrollToFirstNewImage(images.size)
        }
      }

      override fun onError(error: Throwable) {
        error.message?.let { viewState.showErrorMessage(it) }
      }
    }

  fun saveImage(url: String) {
    imageHelperInteractor
      .saveImage(url.validateUrl())
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ fileName ->
        viewState.fileSavedMessage(fileName)
      }, { _ ->
        viewState.fileNotSavedMessage()
      })
  }

  fun shareImage(url: String) {
    imageHelperInteractor
      .shareImage(url.validateUrl())
      .autoDisposable(event(PresenterLifecycle.DETACH_VIEW))
      .subscribe({
        Timber.d("Image sharing request launched.")
      }, { e ->
        e.message?.let { viewState.showErrorMessage(it) }
      })
  }
}
