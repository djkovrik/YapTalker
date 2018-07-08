package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CanShowLoadingIndicator {

    @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "LoadingIndicator")
    fun showLoadingIndicator()

    @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "LoadingIndicator")
    fun hideLoadingIndicator()
}
