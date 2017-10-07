package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.commons.UpdateAppbarEvent
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.sedsoftware.yaptalker.features.base.PresenterLifecycle
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

  private val newsCategories by lazy {
    settings.getNewsCategories()
  }

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    attachRefreshIndicator({
      // onStart
      viewState.showRefreshing()
    }, {
      // onFinish
      viewState.hideRefreshing()
      viewState.hideFab()
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
        .filter { newsItem -> newsCategories.contains(newsItem.forumLink) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onNext
          newsItem ->
          onLoadingSuccess(newsItem)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  fun updateTitle(title: String) {
    pushAppEvent(UpdateAppbarEvent(title))
  }

  fun handleFabVisibility(isFabShown: Boolean, diff: Int) {
    when {
      isFabShown && diff > 0 -> viewState.hideFab()
      !isFabShown && diff < 0 -> viewState.showFab()
    }
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
