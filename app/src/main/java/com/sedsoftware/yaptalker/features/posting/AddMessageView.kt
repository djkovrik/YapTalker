package com.sedsoftware.yaptalker.features.posting

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface AddMessageView : MvpView {

  fun insertTag(tag: String)

  fun insertTags(openingTag: String, closingTag: String)

  fun showErrorMessage(message: String)
}
