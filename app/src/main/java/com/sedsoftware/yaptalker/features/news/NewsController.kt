package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.NewsItem
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_news.view.*

class NewsController : BaseController(), NewsView {

  @InjectPresenter
  lateinit var newsPresenter: NewsPresenter

  lateinit var newsAdapter: NewsAdapter

  override fun getLayoutId() = R.layout.controller_news

  override fun onViewBound(view: View) {

    newsAdapter = NewsAdapter()
    newsAdapter.setHasStableIds(true)

    with(view.refresh_layout) {
      setOnRefreshListener { newsPresenter.loadNews() }
    }

    with(view.news_list) {
      setHasFixedSize(true)
      val manager = LinearLayoutManager(context)
      layoutManager = manager
      adapter = newsAdapter
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

  override fun showNews() {
    newsPresenter.loadNews()
  }

  override fun setNews(news: List<NewsItem>) {
    newsAdapter.setNews(news)
  }

  override fun addNews(news: List<NewsItem>) {
  }

}