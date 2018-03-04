package com.sedsoftware.yaptalker.presentation.features.gallery

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.imagedisplay.SaveImage
import com.sedsoftware.yaptalker.domain.interactor.imagedisplay.ShareImage
import com.sedsoftware.yaptalker.domain.interactor.topic.GetChosenTopicGallery
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.ConnectionState
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.mappers.TopicGalleryModelMapper
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
  private val getTopicGalleryUseCase: GetChosenTopicGallery,
  private val galleryMapper: TopicGalleryModelMapper,
  private val saveImageUseCase: SaveImage,
  private val shareImageUseCase: ShareImage
) : BasePresenter<TopicGalleryView>() {

  companion object {
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private val postsPerPage = settings.getMessagesPerPage()
  private var currentForumId = 0
  private var currentTopicId = 0
  private var currentPage = 1
  private var totalPages = 1
  private var currentTitleLabel = ""

  fun loadTopicGallery(forumId: Int, topicId: Int, startingPost: Int = 0) {
    currentForumId = forumId
    currentTopicId = topicId

    currentPage = when {
      startingPost != 0 -> startingPost / postsPerPage + 1
      else -> 1
    }

    loadTopicCurrentPageGallery()
  }

  fun loadMoreImages() {
    currentPage++
    loadTopicCurrentPageGallery()
  }

  private fun loadTopicCurrentPageGallery() {

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage

    getTopicGalleryUseCase
      .execute(GetChosenTopicGallery.Params(currentForumId, currentTopicId, startingPost))
      .subscribeOn(Schedulers.io())
      .map(galleryMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe { setConnectionState(ConnectionState.LOADING) }
      .doOnError { setConnectionState(ConnectionState.ERROR) }
      .doOnSuccess { setConnectionState(ConnectionState.COMPLETED) }
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

        if (images.isNotEmpty()) {
          viewState.scrollToFirstNewImage(images.size)
        }
      }

      override fun onError(error: Throwable) {
        error.message?.let { viewState.showErrorMessage(it) }
      }
    }

  fun saveImage(url: String) {
    saveImageUseCase
      .execute(SaveImage.Params(url.validateUrl()))
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
    shareImageUseCase
      .execute(ShareImage.Params(url.validateUrl()))
      .autoDisposable(event(PresenterLifecycle.DETACH_VIEW))
      .subscribe({
        Timber.d("Image sharing request launched.")
      }, { e ->
        e.message?.let { viewState.showErrorMessage(it) }
      })
  }
}
