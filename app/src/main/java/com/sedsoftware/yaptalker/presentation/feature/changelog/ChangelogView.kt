package com.sedsoftware.yaptalker.presentation.feature.changelog

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseLoadingView

@StateStrategyType(SkipStrategy::class)
interface ChangelogView : BaseLoadingView {

  fun displayChangelog(markdown: String)
}
