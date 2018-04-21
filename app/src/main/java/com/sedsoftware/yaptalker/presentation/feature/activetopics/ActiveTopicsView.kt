package com.sedsoftware.yaptalker.presentation.feature.activetopics

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface ActiveTopicsView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendActiveTopicItem(topic: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearActiveTopicsList()

  fun updateCurrentUiState()

  fun scrollToViewTop()

  fun showCantLoadPageMessage(page: Int)
}
