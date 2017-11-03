package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.ForumItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface ForumsView : BaseView {

  fun clearForumsList()

  fun appendForumItem(item: ForumItem)

  fun appendForumsList(list: List<ForumItem>)
}
