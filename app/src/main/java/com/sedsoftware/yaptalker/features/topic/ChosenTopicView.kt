package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.model.TopicPost

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenTopicView : MvpView {

  fun showRefreshing()

  fun hideRefreshing()

  fun showErrorMessage(message: String)

  fun refreshPosts(posts: List<TopicPost>)

  fun setNavigationPagesLabel(page: Int, totalPages: Int)

  fun setIfNavigationBackEnabled(isEnabled: Boolean)

  fun setIfNavigationForwardEnabled(isEnabled: Boolean)

  fun showGoToPageDialog(maxPages: Int)

  fun showCantLoadPageMessage(page: Int)

  fun scrollToViewTop()

  fun setAppbarTitle(title: String)

  fun hideNavigationPanel()

  fun hideNavigationPanelWithoutAnimation()

  fun showNavigationPanel()
}
