package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class NewsPresenter : BasePresenter<NewsView>() {

  companion object {
    const val NEWS_PER_PAGE = 50
  }

  @Inject
  lateinit var yapDataManager: YapDataManager

  @Inject
  lateinit var titleChannel: BehaviorRelay<String>

  private var currentPage = 0
  private var backToFirstPage = false

  init {
    YapTalkerApp.appComponent.inject(this)
  }

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
        .subscribe({
          // onNext
          newsItem: NewsItem ->
          onLoadingSuccess(newsItem)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
        .apply { unsubscribeOnDestroy(this) }
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
    viewState.hideFab()
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
