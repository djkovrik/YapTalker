package com.sedsoftware.yaptalker.features.news

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.data.remote.yap.YapRequestState
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

  private var currentPage = 0
  private var backToFirstPage = false

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    attachRefreshIndicator()
  }

  fun checkSavedState(savedViewState: Bundle?, key: String) {
    if (savedViewState != null && savedViewState.containsKey(key)) {
      val news = savedViewState.getParcelableArrayList<NewsItem>(key)
      viewState.refreshNews(news)
    } else {
      loadNews(true)
    }
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

    val subscription =
        yapDataManager
            .getNews(currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              // onSuccess
              newsList: List<NewsItem> ->
              onLoadingSuccess(newsList)
            }, {
              // onError
              throwable ->
              onLoadingError(throwable)
            })

    unsubscribeOnDestroy(subscription)
  }

  fun onLoadingSuccess(news: List<NewsItem>) {
    if (backToFirstPage) {
      viewState.refreshNews(news)
    } else {
      viewState.appendNews(news)
    }
  }

  fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }

  // TODO() Move attachRefreshIndicator to presenter base class,
  // add higher order funcs for loading and load ending
  private fun attachRefreshIndicator() {
    val subscription =
        yapDataManager.requestState.subscribe { state: Long ->
          when (state) {
            YapRequestState.LOADING -> {
              viewState.showRefreshing()
            }
            YapRequestState.COMPLETED,
            YapRequestState.ERROR -> {
              viewState.hideRefreshing()
            }
          }
        }

    unsubscribeOnDestroy(subscription)
  }
}
