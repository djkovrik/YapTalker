package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CanUpdateUiState {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState()
}
