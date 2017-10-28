package com.sedsoftware.yaptalker.features.posting

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface AddMessageView : BaseView {

  fun insertTag(tag: String)

  fun insertTags(openingTag: String, closingTag: String)

  fun showLinkParametersDialogs()
}
