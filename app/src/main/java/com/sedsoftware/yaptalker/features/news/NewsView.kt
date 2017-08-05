package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.NewsItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : MvpView {

  fun onStartLoading()

  fun onFinishLoading()

  fun showRefreshing()

  fun hideRefreshing()

  fun showErrorMessage(message: String)

  fun setNews(news: List<NewsItem>)

  fun addNews(news: List<NewsItem>)
}