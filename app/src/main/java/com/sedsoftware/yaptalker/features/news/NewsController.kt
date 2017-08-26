package com.sedsoftware.yaptalker.features.news

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.InfiniteScrollListener
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setAppColorScheme
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.features.base.BaseController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_news.view.*

class NewsController : BaseController(), NewsView {

  @InjectPresenter
  lateinit var newsPresenter: NewsPresenter

  private lateinit var newsAdapter: NewsAdapter

  override val controllerLayoutId: Int
    get() = R.layout.controller_news

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    newsAdapter = NewsAdapter()
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

  override fun refreshNews(news: List<NewsItem>) {
    newsAdapter.clearAndAddNews(news)
  }

  override fun appendNews(news: List<NewsItem>) {
    newsAdapter.addNews(news)
  }
}
