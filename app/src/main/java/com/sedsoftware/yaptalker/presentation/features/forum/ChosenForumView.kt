package com.sedsoftware.yaptalker.presentation.features.forum

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface ChosenForumView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun addTopicItem(entity: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearTopicsList()

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState(forumTitle: String)

  fun initiateForumLoading()

  fun scrollToViewTop()

  fun showCantLoadPageMessage(page: Int)
}
