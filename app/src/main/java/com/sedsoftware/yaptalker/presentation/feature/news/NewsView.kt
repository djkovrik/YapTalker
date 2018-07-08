package com.sedsoftware.yaptalker.presentation.feature.news

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel

@StateStrategyType(SkipStrategy::class)
interface NewsView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

    @StateStrategyType(AddToEndStrategy::class)
    fun appendNewsItem(item: NewsItemModel)

    @StateStrategyType(SingleStateStrategy::class)
    fun clearNewsList()

    fun browseExternalResource(url: String)

    fun showFab()

    fun hideFab()

    fun showBlacklistRequest()

    fun showTopicBlacklistedMessage()

    fun removeBlacklistedTopicFromList(topic: NewsItemModel)
}
