package com.sedsoftware.yaptalker.presentation.features.activetopics

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface ActiveTopicsView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendActiveTopicItem(topic: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearActiveTopicsList()

  fun updateCurrentUiState()

  fun scrollToViewTop()

  fun showCantLoadPageMessage(page: Int)
}
