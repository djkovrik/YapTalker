package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.sedsoftware.yaptalker.data.parsing.NewsItem
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
    loadNews(loadFromFirstPage = true)
  }

  override fun attachView(view: NewsView?) {
    super.attachView(view)
    viewState.updateAppbarTitle()
  }

  fun onScrollFabVisibility(diff: Int) {
    when {
      diff > 0 -> viewState.hideFab()
      diff < 0 -> viewState.showFab()
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

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
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
          onNewsItemLoaded(newsItem)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onNewsItemLoaded(newsItem: NewsItem) {
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
