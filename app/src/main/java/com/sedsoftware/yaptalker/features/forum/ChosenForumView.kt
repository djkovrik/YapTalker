package com.sedsoftware.yaptalker.features.forum

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.ForumPage

@StateStrategyType(SkipStrategy::class)
interface ChosenForumView : BaseView {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun displayForumPage(page: ForumPage)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateAppbarTitle(title: String)

  fun initiateForumLoading()

  fun scrollToViewTop()

  fun showCantLoadPageMessage(page: Int)
}
