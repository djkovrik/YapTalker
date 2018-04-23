package com.sedsoftware.yaptalker.presentation.feature.search

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState

@StateStrategyType(SkipStrategy::class)
interface SearchFormView : MvpView, CanUpdateUiState
