package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.TopicPage

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenTopicView : BaseView {

  fun displayTopicPage(page: TopicPage)

  fun scrollToViewTop()

  fun showFab(shouldShow: Boolean)

  fun hideFabWithoutAnimation()

  @StateStrategyType(SkipStrategy::class)
  fun handleBookmarkButtonVisibility(shouldShow: Boolean)

  @StateStrategyType(SkipStrategy::class)
  fun showCantLoadPageMessage(page: Int)

  fun showUserProfile(userId: Int)

  fun shareTopic(title: String, topicPage: Int)

  @StateStrategyType(SkipStrategy::class)
  fun showBookmarkAddedMessage()

  @StateStrategyType(SkipStrategy::class)
  fun showUnknownErrorMessage()
}
