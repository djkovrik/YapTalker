package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.sedsoftware.yaptalker.features.base.BasePresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class NewsPresenter : BasePresenter<NewsView>() {

  companion object {
    private const val NEWS_PER_PAGE = 50
  }

  private var currentPage = 0
  private var backToFirstPage = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    viewState.showFab()

    attachRefreshIndicator(yapDataManager.requestState, {
      // onStart
      viewState.showRefreshing()
    }, {
      // onFinish
      viewState.hideRefreshing()
    })
  }

  override fun attachView(view: NewsView?) {
    super.attachView(view)
    viewState.updateAppbarTitle()
    viewState.hideFabWithoutAnimation()
  }

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

    yapDataManager
        .getNews(currentPage)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(BasePresenterLifecycle.DESTROY))
        .subscribe({
          // onNext
          newsItem: NewsItem ->
          onLoadingSuccess(newsItem)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  fun updateTitle(title: String) {
    pushAppbarTitle(titleChannel, title)
  }

  fun handleFabVisibility(diff: Int) {
    when {
      diff > 0 -> viewState.hideFab()
      else -> viewState.showFab()
    }
  }

  fun scrollToTop() {
    viewState.scrollListToTop()
  }

  private fun onLoadingSuccess(newsItem: NewsItem) {
    if (backToFirstPage) {
      viewState.clearNewsList()
      backToFirstPage = false
    }
    viewState.appendNewsItem(newsItem)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
