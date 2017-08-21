package com.sedsoftware.yaptalker.features.forum

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.model.Topic

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenForumView : MvpView {

  fun showRefreshing()

  fun hideRefreshing()

  fun showErrorMessage(message: String)

  fun refreshTopics(topics: List<Topic>)

  fun setIfNavigationPanelVisible(isVisible: Boolean)

  fun setNavigationPagesLabel(page: Int, totalPages: Int)

  fun setIfNavigationBackEnabled(isEnabled: Boolean)

  fun setIfNavigationForwardEnabled(isEnabled: Boolean)
}
