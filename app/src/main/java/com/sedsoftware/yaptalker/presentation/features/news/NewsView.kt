package com.sedsoftware.yaptalker.presentation.features.news

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseLoadingView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface NewsView : BaseLoadingView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendNewsItem(entity: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearNewsList()

  fun updateCurrentUiState()

  fun showFab()

  fun hideFab()
}
