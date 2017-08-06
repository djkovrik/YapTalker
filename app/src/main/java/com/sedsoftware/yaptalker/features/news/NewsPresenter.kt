package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.NewsItem
import com.sedsoftware.yaptalker.data.remote.YapDataManager
import com.sedsoftware.yaptalker.data.remote.YapRequestState
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class NewsPresenter : BasePresenter<NewsView>() {

  @Inject
  lateinit var yapDataManager: YapDataManager

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    attachRefreshIndicator()
  }

  override fun attachView(view: NewsView?) {
    super.attachView(view)
    viewState.showNews()
  }

  fun loadNews() {
    loadData(0)
  }

  fun attachRefreshIndicator() {
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

  fun loadData(startNumber: Int) {

    val subscription =
        yapDataManager
            .getNews(startNumber)
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

  private fun onLoadingSuccess(news: List<NewsItem>) {
    viewState.setNews(news)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}