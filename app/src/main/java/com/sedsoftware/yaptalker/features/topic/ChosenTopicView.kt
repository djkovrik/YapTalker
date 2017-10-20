package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.model.TopicPost

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenTopicView : BaseView {

  fun refreshPosts(posts: List<TopicPost>)

  fun setNavigationPagesLabel(page: Int, totalPages: Int)

  fun setIfNavigationBackEnabled(isEnabled: Boolean)

  fun setIfNavigationForwardEnabled(isEnabled: Boolean)

  fun scrollToViewTop()

  fun showGoToPageDialog(maxPages: Int)

  fun showCantLoadPageMessage(page: Int)

  fun showFab(shouldShow: Boolean)

  fun hideFabWithoutAnimation()

  fun showAddMessageActivity(title: String)
}
