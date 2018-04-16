package com.sedsoftware.yaptalker.presentation.features.incubator

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseLoadingView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface IncubatorView : BaseLoadingView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendIncubatorItem(entity: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearIncubatorsList()

  fun updateCurrentUiState()

  fun showFab()

  fun hideFab()
}
