package com.sedsoftware.yaptalker.presentation.feature.topic

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@Suppress("ComplexInterface", "TooManyFunctions")
@StateStrategyType(SkipStrategy::class)
interface ChosenTopicView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendPostItem(item: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearPostsList()

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun setLoggedInState(isLoggedIn: Boolean)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun setTopicKarmaState(isKarmaAvailable: Boolean)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState(title: String)

  fun initiateTopicLoading()

  fun showUserProfile(userId: Int)

  fun shareTopic(title: String, topicPage: Int)

  fun showPostKarmaMenu(postId: Int)

  fun showTopicKarmaMenu()

  fun saveScrollPosition()

  fun restoreScrollPosition()

  fun scrollToViewTop()

  fun showPageSelectionDialog()

  fun showCantLoadPageMessage(page: Int)

  fun showBookmarkAddedMessage()

  fun showPostKarmaChangedMessage(isTopic: Boolean)

  fun showPostAlreadyRatedMessage(isTopic: Boolean)

  fun showUnknownErrorMessage()

  fun blockScreenSleep()

  fun unblockScreenSleep()

  fun showFab()

  fun hideFab()

  fun browseExternalResource(url: String)
}
