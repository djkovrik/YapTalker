package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseViewWithLoading
import com.sedsoftware.yaptalker.data.model.ForumItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface ForumsView : BaseViewWithLoading {

  fun clearForumsList()

  fun appendForumItem(item: ForumItem)

  fun appendForumsList(list: List<ForumItem>)
}
