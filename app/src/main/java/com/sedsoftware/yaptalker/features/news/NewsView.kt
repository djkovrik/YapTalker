package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.NewsItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : MvpView {

  fun showRefreshing()

  fun hideRefreshing()

  fun showErrorMessage(message: String)

  fun showNews()

  fun setNews(news: List<NewsItem>)

  fun addNews(news: List<NewsItem>)
}