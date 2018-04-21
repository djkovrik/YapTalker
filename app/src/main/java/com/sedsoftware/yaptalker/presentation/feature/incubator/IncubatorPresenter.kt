package com.sedsoftware.yaptalker.presentation.feature.incubator

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.common.GetVideoThumbnail
import com.sedsoftware.yaptalker.domain.interactor.incubator.GetIncubatorTopics
import com.sedsoftware.yaptalker.presentation.base.BaseLoadingPresenter
import com.sedsoftware.yaptalker.presentation.base.enums.ConnectionState
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mapper.IncubatorModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class IncubatorPresenter @Inject constructor(
  private val router: Router,
  private val getIncubatorTopics: GetIncubatorTopics,
  private val getVideoThumbnail: GetVideoThumbnail,
  private val incubatorModelMapper: IncubatorModelMapper
) : BaseLoadingPresenter<IncubatorView>() {

  companion object {
    private const val ITEMS_PER_PAGE = 50
  }

  private var currentPage = 0
  private var backToFirstPage = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadIncubator(loadFromFirstPage = true)
  }

  override fun attachView(view: IncubatorView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  fun handleFabVisibility(diff: Int) {
    when {
      diff > 0 -> viewState.hideFab()
      diff < 0 -> viewState.showFab()
    }
  }

  fun requestThumbnail(videoUrl: String): Single<String> =
    getVideoThumbnail
      .execute(GetVideoThumbnail.Params(videoUrl))

  fun loadIncubator(loadFromFirstPage: Boolean) {

    backToFirstPage = loadFromFirstPage

    if (backToFirstPage) {
      currentPage = 0
    } else {
      currentPage += ITEMS_PER_PAGE
    }

    loadDataForCurrentPage()
  }

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun navigateToChosenVideo(html: String) {
    router.navigateTo(NavigationScreen.VIDEO_DISPLAY_SCREEN, html)
  }

  fun navigateToChosenImage(url: String) {
    router.navigateTo(NavigationScreen.IMAGE_DISPLAY_SCREEN, url)
  }

  private fun loadDataForCurrentPage() {
    getIncubatorTopics
      .execute(GetIncubatorTopics.Params(pageNumber = currentPage))
      .subscribeOn(Schedulers.io())
      .map(incubatorModelMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe { setConnectionState(ConnectionState.LOADING) }
      .doOnError { setConnectionState(ConnectionState.ERROR) }
      .doOnComplete { setConnectionState(ConnectionState.COMPLETED) }
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getIncubatorObserver())
  }

  private fun getIncubatorObserver() =
    object : DisposableObserver<YapEntity>() {

      override fun onNext(item: YapEntity) {
        if (backToFirstPage) {
          viewState.clearIncubatorsList()
          backToFirstPage = false
        }

        viewState.appendIncubatorItem(item)
      }

      override fun onComplete() {
        Timber.i("Incubator page loading completed.")
      }

      override fun onError(e: Throwable) {
        e.message?.let { viewState.showErrorMessage(it) }
      }
    }
}
