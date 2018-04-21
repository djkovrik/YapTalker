package com.sedsoftware.yaptalker.presentation.feature.search

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface SearchFormView : MvpView {

  fun updateCurrentUiState()
}
