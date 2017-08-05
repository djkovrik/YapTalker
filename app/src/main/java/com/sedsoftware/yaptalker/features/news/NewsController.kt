package com.sedsoftware.yaptalker.features.news

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.NewsItem
import com.sedsoftware.yaptalker.features.base.BaseController
import timber.log.Timber

class NewsController : BaseController(), NewsView {

  @InjectPresenter
  lateinit var newsPresenter: NewsPresenter

  @BindView(R.id.refresh_layout)
  lateinit var refreshLayout: SwipeRefreshLayout

  @BindView(R.id.news_list)
  lateinit var newsList: RecyclerView

  override fun getLayoutId() = R.layout.controller_news

  override fun onViewBound(view: View) {
  }

  override fun onStartLoading() {
    refreshLayout.isEnabled = false
  }

  override fun onFinishLoading() {
    refreshLayout.isEnabled = true
  }

  override fun showRefreshing() {
    refreshLayout.isRefreshing = true
  }

  override fun hideRefreshing() {
    refreshLayout.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {

  }

  override fun setNews(news: List<NewsItem>) {
    for (newsItem in news) {
      Timber.d("Title: " + newsItem.topic.title)
    }
  }

  override fun addNews(news: List<NewsItem>) {
  }

}