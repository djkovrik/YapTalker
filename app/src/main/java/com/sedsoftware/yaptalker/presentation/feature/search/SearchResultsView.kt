package com.sedsoftware.yaptalker.presentation.feature.search

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel

@StateStrategyType(SkipStrategy::class)
interface SearchResultsView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendSearchResultsTopicItem(item: DisplayedItemModel)
}
