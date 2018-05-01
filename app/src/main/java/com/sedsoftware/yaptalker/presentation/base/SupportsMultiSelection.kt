package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SupportsMultiSelection {

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "mode")
  fun enterSelectionMode()

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "mode")
  fun exitSelectionMode()

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "item")
  fun selectItem(position: Int)

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "item")
  fun deselectItem(position: Int)

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "item")
  fun deselectAllItems()
}
