package com.sedsoftware.yaptalker.presentation.feature.blacklist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.base.BlacklistedTopicModel

@StateStrategyType(SkipStrategy::class)
interface BlacklistView : MvpView, CanShowErrorMessage, CanUpdateUiState {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendBlacklistItem(topic: BlacklistedTopicModel)

  fun showDeleteConfirmationDialog(topicId: Int)
}
