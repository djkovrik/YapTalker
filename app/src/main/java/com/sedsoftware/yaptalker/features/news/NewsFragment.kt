package com.sedsoftware.yaptalker.features.news

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.InfiniteScrollListener
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.hideBeyondScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.showFromScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.parsing.NewsItem
import com.sedsoftware.yaptalker.features.news.adapter.NewsAdapter
import com.sedsoftware.yaptalker.features.news.adapter.NewsItemClickListener
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : BaseFragment(), NewsView, NewsItemClickListener {

  companion object {
    private const val INITIAL_FAB_OFFSET = 250f

    fun getNewInstance() = NewsFragment()
  }

  @InjectPresenter
  lateinit var newsPresenter: NewsPresenter

  override val layoutId: Int
    get() = R.layout.fragment_news

  private lateinit var newsAdapter: NewsAdapter
  private var isFabShown = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    newsAdapter = NewsAdapter(this)

    newsAdapter.setHasStableIds(true)

    with(news_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = newsAdapter

      setHasFixedSize(true)
      clearOnScrollListeners()

      addOnScrollListener(InfiniteScrollListener({
        newsPresenter.loadNews(loadFromFirstPage = false)
      }, linearLayout))
    }

    refresh_layout.setIndicatorColorScheme()

    newsPresenter.loadNews(true)
    context?.stringRes(R.string.nav_drawer_main_page)?.let { newsPresenter.updateAppbarTitle(it) }
  }

  override fun subscribeViews() {

    RxSwipeRefreshLayout
        .refreshes(refresh_layout)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { newsPresenter.loadNews(loadFromFirstPage = true) }

    RxRecyclerView
        .scrollEvents(news_list)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { event -> newsPresenter.handleFabVisibility(isFabShown, event.dy()) }

    RxView
        .clicks(news_fab)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { newsPresenter.loadNews(loadFromFirstPage = true) }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator(shouldShow: Boolean) {
    refresh_layout.isRefreshing = shouldShow
  }

  override fun clearNewsList() {
    newsAdapter.clearNews()
  }

  override fun appendNewsItem(item: NewsItem) {
    newsAdapter.addNewsItem(item)
  }

  override fun showFab(shouldShow: Boolean) {

    if (shouldShow == isFabShown) {
      return
    }

    if (shouldShow) {
      news_fab?.let { fab ->
        fab.showFromScreenEdge()
        isFabShown = true
      }
    } else {
      news_fab?.let { fab ->
        val offset = fab.height + fab.paddingTop + fab.paddingBottom
        fab.hideBeyondScreenEdge(offset.toFloat())
        isFabShown = false
      }
    }
  }

  override fun hideFabWithoutAnimation() {
    news_fab.translationY = INITIAL_FAB_OFFSET
    isFabShown = false
  }

  override fun onNewsItemClick(link: String, forumLink: String) {
    if (link.contains("yaplakal.com")) {
      newsPresenter.navigateToChosenTopic(Triple(forumLink.getLastDigits(), link.getLastDigits(), 0))
    }
  }
}
