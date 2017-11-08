package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.ForumItem

interface ForumsView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendForumItem(item: ForumItem)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearForumsList()
}
