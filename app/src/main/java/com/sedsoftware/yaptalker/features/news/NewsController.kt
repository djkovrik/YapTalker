package com.sedsoftware.yaptalker.features.news

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.InfiniteScrollListener
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_news.view.*

class NewsController : BaseController(), NewsView {

  companion object {
    private const val NEWS_LIST_KEY = "NEWS_LIST_KEY"
  }

  @InjectPresenter
  lateinit var newsPresenter: NewsPresenter

  lateinit var newsAdapter: NewsAdapter

  override val controllerLayoutId: Int
    get() = R.layout.controller_news


  override fun onViewBound(view: View, savedViewState: Bundle?) {

    newsAdapter = NewsAdapter()
    newsAdapter.setHasStableIds(true)

    with(view.refresh_layout) {
      setOnRefreshListener { newsPresenter.loadNews(loadFromFirstPage = true) }
      setColorSchemeColors(
          view.context.color(R.color.colorPrimary),
          view.context.color(R.color.colorAccent),
          view.context.color(R.color.colorPrimaryDark),
          view.context.color(R.color.colorAccentDark)
      )
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

    // Restore news list or force reload
    if (savedViewState != null && savedViewState.containsKey(NEWS_LIST_KEY)) {
      val news = savedViewState.getParcelableArrayList<NewsItem>(NEWS_LIST_KEY)
      newsAdapter.clearAndAddNews(news)
    } else {
      newsPresenter.loadNews(loadFromFirstPage = true)
    }
  }

  override fun onSaveViewState(view: View, outState: Bundle) {
    super.onSaveViewState(view, outState)
    val news = newsAdapter.getNews()
    if (news.size > 0) {
      outState.putParcelableArrayList(NEWS_LIST_KEY, news)
    }
  }

  override fun onDetach(view: View) {
    super.onDetach(view)
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
