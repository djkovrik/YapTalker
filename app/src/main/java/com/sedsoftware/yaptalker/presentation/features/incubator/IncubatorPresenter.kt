package com.sedsoftware.yaptalker.presentation.features.incubator

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetIncubatorTopics
import com.sedsoftware.yaptalker.domain.interactor.GetVideoThumbnail
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.ConnectionState
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mappers.IncubatorModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
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
) : BasePresenter<IncubatorView>() {

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

  fun requestThumbnail(videoUrl: String): Observable<String> =
      getVideoThumbnail
          .buildUseCaseObservable(GetVideoThumbnail.Params(videoUrl))

  fun loadIncubator(loadFromFirstPage: Boolean) {

    backToFirstPage = loadFromFirstPage

    if (backToFirstPage) {
      currentPage = 0
    } else {
      currentPage += ITEMS_PER_PAGE
    }

    loadDataForCurrentPage()
  }

  private fun loadDataForCurrentPage() {
    getIncubatorTopics
        .buildUseCaseObservable(GetIncubatorTopics.Params(pageNumber = currentPage))
        .subscribeOn(Schedulers.io())
        .map { incubatorItem: BaseEntity -> incubatorModelMapper.transform(incubatorItem) }
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

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun navigateToChosenVideo(html: String) {
    router.navigateTo(NavigationScreen.VIDEO_DISPLAY_SCREEN, html)
  }

  fun navigateToChosenImage(url: String) {
    router.navigateTo(NavigationScreen.IMAGE_DISPLAY_SCREEN, url)
  }
}
