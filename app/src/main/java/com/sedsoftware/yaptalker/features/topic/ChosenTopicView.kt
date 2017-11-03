package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.TopicPage

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenTopicView : BaseView {

  fun displayTopicPage(page: TopicPage)

  fun scrollToViewTop()

  fun showFab(shouldShow: Boolean)

  fun hideFabWithoutAnimation()

  fun handleBookmarkButtonVisibility(shouldShow: Boolean)

  fun showCantLoadPageMessage(page: Int)

  fun showUserProfile(userId: Int)

  fun shareTopic(title: String, topicPage: Int)

  fun showBookmarkAddedMessage()

  fun showUnknownErrorMessage()
}
