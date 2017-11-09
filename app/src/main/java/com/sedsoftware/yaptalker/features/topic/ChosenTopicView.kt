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

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun setLoggedInState(isLoggedIn: Boolean)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun setTopicKarmaState(isKarmaAvailable: Boolean)

  fun initiateTopicLoading()

  fun showUserProfile(userId: Int)

  fun shareTopic(title: String, topicPage: Int)

  fun displayPostContextMenu(postId: String)

  fun scrollToViewTop()

  fun showCantLoadPageMessage(page: Int)

  fun showBookmarkAddedMessage()

  fun showUnknownErrorMessage()
}
