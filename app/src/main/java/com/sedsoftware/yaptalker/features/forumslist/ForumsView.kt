package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.model.ForumItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface ForumsView : MvpView {

  fun showForums(forums: List<ForumItem>)

  fun showErrorMessage(message: String)
}