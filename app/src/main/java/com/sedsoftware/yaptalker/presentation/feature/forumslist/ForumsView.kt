package com.sedsoftware.yaptalker.presentation.feature.forumslist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.model.YapEntity

interface ForumsView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendForumItem(item: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearForumsList()

  @StateStrategyType(SkipStrategy::class)
  fun updateCurrentUiState()
}
