package com.sedsoftware.yaptalker.features.forum

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseViewWithLoading
import com.sedsoftware.yaptalker.data.model.Topic

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenForumView : BaseViewWithLoading {

  fun refreshTopics(topics: List<Topic>)

  fun setNavigationPagesLabel(page: Int, totalPages: Int)

  fun setIfNavigationBackEnabled(isEnabled: Boolean)

  fun setIfNavigationForwardEnabled(isEnabled: Boolean)

  fun scrollToViewTop()

  fun showGoToPageDialog(maxPages: Int)

  fun showCantLoadPageMessage(page: Int)
}
