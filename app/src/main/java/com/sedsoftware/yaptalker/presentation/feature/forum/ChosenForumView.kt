package com.sedsoftware.yaptalker.presentation.feature.forum

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface ChosenForumView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

  @StateStrategyType(AddToEndStrategy::class)
  fun addTopicItem(item: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearTopicsList()

  fun initiateForumLoading()

  fun scrollToViewTop()

  fun showPageSelectionDialog()

  fun showCantLoadPageMessage(page: Int)
}
