package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CanShowErrorMessage {

    @StateStrategyType(SkipStrategy::class)
    fun showErrorMessage(message: String)
}
