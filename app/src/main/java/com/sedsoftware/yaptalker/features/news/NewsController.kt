package com.sedsoftware.yaptalker.features.news

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.InfiniteScrollListener
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.hideBeyondScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setAppColorScheme
import com.sedsoftware.yaptalker.commons.extensions.showFromScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.features.base.BaseController
import com.sedsoftware.yaptalker.features.topic.ChosenTopicController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_news.view.*

class NewsController : BaseController(), NewsView {

  companion object {
    // TODO() Calculate offsets in runtime
    private const val FAB_OFFSET = 200f
  }

  @InjectPresenter
  lateinit var newsPresenter: NewsPresenter

  private lateinit var newsAdapter: NewsAdapter
  private var isFabShown = true

  override val controllerLayoutId: Int
    get() = R.layout.controller_news

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    newsAdapter = NewsAdapter { link, forumLink ->

      if (link.contains("yaplakal.com")) {
        val topicId = link.getLastDigits()
        val forumId = forumLink.getLastDigits()
        val bundle = Bundle()

        bundle.putInt(ChosenTopicController.TOPIC_ID_KEY, topicId)
        bundle.putInt(ChosenTopicController.FORUM_ID_KEY, forumId)
        router.pushController(
            RouterTransaction.with(ChosenTopicController(bundle))
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
      }
    }

    newsAdapter.setHasStableIds(true)

    with(view.refresh_layout) {
      setAppColorScheme()
    }

    with(view.news_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = newsAdapter

      setHasFixedSize(true)
      clearOnScrollListeners()

      addOnScrollListener(InfiniteScrollListener({
        newsPresenter.loadNews(loadFromFirstPage = false)
      }, linearLayout))
    }

    newsPresenter.loadNews(true)
  }

  override fun subscribeViews(parent: View) {

    parent.refresh_layout?.let {
      RxSwipeRefreshLayout
          .refreshes(parent.refresh_layout)
          .autoDisposeWith(scopeProvider)
          .subscribe { newsPresenter.loadNews(loadFromFirstPage = true) }
    }

    parent.news_list?.let {
      RxRecyclerView
          .scrollEvents(parent.news_list)
          .autoDisposeWith(scopeProvider)
          .subscribe { event -> newsPresenter.handleFabVisibility(diff = event.dy()) }
    }

    parent.news_fab?.let {
      RxView
          .clicks(parent.news_fab)
          .autoDisposeWith(scopeProvider)
          .subscribe { newsPresenter.scrollToTop() }
    }
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    view.news_list.adapter = null
  }

  override fun showRefreshing() {
    view?.refresh_layout?.isRefreshing = true
  }

  override fun hideRefreshing() {
    view?.refresh_layout?.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun clearNewsList() {
    newsAdapter.clearNews()
  }

  override fun appendNewsItem(item: NewsItem) {
    newsAdapter.addNewsItem(item)
  }

  override fun updateAppbarTitle() {
    view?.context?.let {
      newsPresenter.updateTitle(it.stringRes(R.string.nav_drawer_main_page))
    }
  }

  override fun hideFab() {
    if (!isFabShown) {
      return
    }

    view?.news_fab?.let { fab ->
      fab.hideBeyondScreenEdge(FAB_OFFSET)
      isFabShown = false
    }
  }

  override fun hideFabWithoutAnimation() {
    view?.news_fab?.translationY = FAB_OFFSET
  }

  override fun showFab() {
    if (isFabShown) {
      return
    }

    view?.news_fab?.let { fab ->
      fab.showFromScreenEdge()
      isFabShown = true
    }
  }

  override fun scrollListToTop() {
    view?.news_list?.smoothScrollToPosition(0)
  }
}
