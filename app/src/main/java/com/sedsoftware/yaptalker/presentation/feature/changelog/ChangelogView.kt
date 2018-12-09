package com.sedsoftware.yaptalker.presentation.feature.changelog

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator

@StateStrategyType(SkipStrategy::class)
interface ChangelogView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator {
    fun displayChangelog(markdown: String)
}
