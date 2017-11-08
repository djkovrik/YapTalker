package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.NewsItem

@StateStrategyType(SkipStrategy::class)
interface NewsView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendNewsItem(item: NewsItem)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearNewsList()

  fun showFab()

  fun hideFab()
}
