package com.sedsoftware.yaptalker.presentation.features.search

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseLoadingView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface SearchResultsView : BaseLoadingView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendSearchResultsTopicItem(topic: YapEntity)

  fun updateCurrentUiState()
}
