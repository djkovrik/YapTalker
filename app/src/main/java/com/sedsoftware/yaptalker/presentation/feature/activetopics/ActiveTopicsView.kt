package com.sedsoftware.yaptalker.presentation.feature.activetopics

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel

@StateStrategyType(SkipStrategy::class)
interface ActiveTopicsView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendActiveTopicItem(item: DisplayedItemModel)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearActiveTopicsList()

  fun scrollToViewTop()

  fun showPageSelectionDialog()

  fun showCantLoadPageMessage(page: Int)
}
