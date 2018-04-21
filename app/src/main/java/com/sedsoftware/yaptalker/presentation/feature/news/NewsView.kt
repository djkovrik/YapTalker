package com.sedsoftware.yaptalker.presentation.feature.news

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface NewsView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendNewsItem(entity: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearNewsList()

  fun updateCurrentUiState()

  fun showFab()

  fun hideFab()
}
