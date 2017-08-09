package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.InfiniteScrollListener
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_news.view.*

class NewsController : BaseController(), NewsView {

  @InjectPresenter
  lateinit var newsPresenter: NewsPresenter

  lateinit var newsAdapter: NewsAdapter

  override fun getLayoutId() = R.layout.controller_news

  override fun onViewBound(view: View) {

    newsAdapter = NewsAdapter(view.context)
    newsAdapter.setHasStableIds(true)

    with(view.refresh_layout) {
      setOnRefreshListener { newsPresenter.loadNews(true) }
    }

    with(view.news_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = newsAdapter

      setHasFixedSize(true)
      clearOnScrollListeners()

      addOnScrollListener(InfiniteScrollListener({
        newsPresenter.loadNews(false)
      }, linearLayout))
    }
  }

  override fun showRefreshing() {
    view?.refresh_layout?.isRefreshing = true
  }

  override fun hideRefreshing() {
    view?.refresh_layout?.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {

  }

  override fun refreshNews(news: List<NewsItem>) {
    newsAdapter.clearAndAddNews(news)
  }

  override fun appendNews(news: List<NewsItem>) {
    newsAdapter.addNews(news)
  }
}