package com.sedsoftware.yaptalker.presentation.features.changelog

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface ChangelogView : BaseView {

  fun displayChangelog(markdown: String)
}
