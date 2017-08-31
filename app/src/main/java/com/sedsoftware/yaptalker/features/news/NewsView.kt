package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.model.NewsItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : MvpView {

  fun showRefreshing()

  fun hideRefreshing()

  fun showErrorMessage(message: String)

  fun clearNewsList()

  fun appendNewsItem(item: NewsItem)

  fun updateAppbarTitle()

  fun hideFab()

  fun hideFabWithoutAnimation()

  fun showFab()

  fun scrollListToTop()
}
