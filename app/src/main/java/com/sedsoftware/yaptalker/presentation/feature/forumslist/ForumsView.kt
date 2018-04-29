package com.sedsoftware.yaptalker.presentation.feature.forumslist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel

interface ForumsView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendForumItem(item: ForumModel)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearForumsList()
}
