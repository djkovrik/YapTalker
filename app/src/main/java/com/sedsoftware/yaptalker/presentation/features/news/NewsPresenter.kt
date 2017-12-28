package com.sedsoftware.yaptalker.presentation.features.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetNewsList
import com.sedsoftware.yaptalker.domain.interactor.GetVideoThumbnail
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mappers.NewsModelMapper
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
class NewsPresenter @Inject constructor(
    private val router: Router,
    private val getNewsListUseCase: GetNewsList,
    private val getVideoThumbnail: GetVideoThumbnail,
    private val newsModelMapper: NewsModelMapper
) : BasePresenter<NewsView>() {

  companion object {
    private const val NEWS_PER_PAGE = 50
  }

  private var currentPage = 0
  private var backToFirstPage = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadNews(loadFromFirstPage = true)
  }

  override fun attachView(view: NewsView?) {
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

  fun loadNews(loadFromFirstPage: Boolean) {

    backToFirstPage = loadFromFirstPage

    if (backToFirstPage) {
      currentPage = 0
    } else {
      currentPage += NEWS_PER_PAGE
    }

    loadDataForCurrentPage()
  }

  private fun loadDataForCurrentPage() {
    getNewsListUseCase
        .buildUseCaseObservable(GetNewsList.Params(pageNumber = currentPage))
        .subscribeOn(Schedulers.io())
        .map { newsItem: BaseEntity -> newsModelMapper.transform(newsItem) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showLoadingIndicator() }
        .doFinally { viewState.hideLoadingIndicator() }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getNewsObserver())
  }

  private fun getNewsObserver() =
      object : DisposableObserver<YapEntity>() {
        override fun onNext(item: YapEntity) {
          if (backToFirstPage) {
            viewState.clearNewsList()
            backToFirstPage = false
          }

          viewState.appendNewsItem(item)
        }

        override fun onComplete() {
          Timber.i("News page loading completed.")
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
