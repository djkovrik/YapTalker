package com.sedsoftware.yaptalker.presentation.feature.search

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface SearchFormView : BaseView {

  fun updateCurrentUiState()
}
