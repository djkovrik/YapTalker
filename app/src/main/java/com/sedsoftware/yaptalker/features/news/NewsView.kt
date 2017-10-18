package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseViewWithLoading
import com.sedsoftware.yaptalker.data.model.NewsItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : BaseViewWithLoading {

  fun clearNewsList()

  fun appendNewsItem(item: NewsItem)

  fun showFab(shouldShow: Boolean)

  fun hideFabWithoutAnimation()
}
