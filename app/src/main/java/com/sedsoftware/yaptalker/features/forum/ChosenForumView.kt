package com.sedsoftware.yaptalker.features.forum

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.model.Topic

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenForumView : BaseView {

  fun refreshTopics(topics: List<Topic>)

  fun scrollToViewTop()
}
