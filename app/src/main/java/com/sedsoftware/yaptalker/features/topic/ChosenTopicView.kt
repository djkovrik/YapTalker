package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.model.TopicPage

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenTopicView : BaseView {

  fun displayTopicPage(page: TopicPage)

  fun scrollToViewTop()

  fun showFab(shouldShow: Boolean)

  fun hideFabWithoutAnimation()

  fun showAddMessageActivity(title: String)

  fun showCantLoadPageMessage(page: Int)

  fun showUserProfile(userId: Int)

  fun shareTopic(title: String)
}
