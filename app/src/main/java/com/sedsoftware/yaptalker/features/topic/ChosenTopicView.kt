package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.TopicPage

@StateStrategyType(SkipStrategy::class)
interface ChosenTopicView : BaseView {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun displayTopicPage(page: TopicPage)

  fun showFab(shouldShow: Boolean)

  fun hideFabWithoutAnimation()

  fun showUserProfile(userId: Int)

  fun shareTopic(title: String, topicPage: Int)

  fun showCantLoadPageMessage(page: Int)

  fun scrollToViewTop()

  fun showBookmarkAddedMessage()

  fun showUnknownErrorMessage()

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun setIfMenuButtonsAvailable(loggedIn: Boolean, karmaAvailable: Boolean)
}
