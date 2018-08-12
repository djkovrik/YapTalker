package com.sedsoftware.yaptalker.presentation.feature.incubator

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.base.IncubatorItemModel

@StateStrategyType(SkipStrategy::class)
interface IncubatorView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

    @StateStrategyType(AddToEndStrategy::class)
    fun appendIncubatorItems(items: List<IncubatorItemModel>)

    @StateStrategyType(SingleStateStrategy::class)
    fun clearIncubatorsList()

    fun browseExternalResource(url: String)

    fun showFab()

    fun hideFab()
}
