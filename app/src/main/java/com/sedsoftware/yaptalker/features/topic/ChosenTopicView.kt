package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseViewWithLoading
import com.sedsoftware.yaptalker.data.model.TopicPost

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenTopicView : BaseViewWithLoading {

  fun showRefreshing()

  fun hideRefreshing()

  fun refreshPosts(posts: List<TopicPost>)

  fun setNavigationPagesLabel(page: Int, totalPages: Int)

  fun setIfNavigationBackEnabled(isEnabled: Boolean)

  fun setIfNavigationForwardEnabled(isEnabled: Boolean)

  fun showGoToPageDialog(maxPages: Int)

  fun showCantLoadPageMessage(page: Int)

  fun scrollToViewTop()

  fun hideNavigationPanel()

  fun showNavigationPanel()

  fun hideFab()

  fun hideFabWithoutAnimation()

  fun showFab()

  fun showAddMessageActivity(title: String)
}
