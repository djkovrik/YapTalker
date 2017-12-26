package com.sedsoftware.yaptalker.presentation.features.forumslist

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

interface ForumsView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendForumItem(item: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearForumsList()

  @StateStrategyType(SkipStrategy::class)
  fun updateCurrentUiState()
}
