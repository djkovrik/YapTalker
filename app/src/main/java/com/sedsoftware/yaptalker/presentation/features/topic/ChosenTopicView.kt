package com.sedsoftware.yaptalker.presentation.features.topic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@Suppress("ComplexInterface")
@StateStrategyType(SkipStrategy::class)
interface ChosenTopicView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendPostItem(post: YapEntity)

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

  fun displayPostKarmaMenu(postId: Int, postPosition: Int)

  fun displayTopicKarmaMenu()

  fun scrollToViewTop()

  fun scrollToPost(position: Int)

  fun showCantLoadPageMessage(page: Int)

  fun showBookmarkAddedMessage()

  fun showPostKarmaChangedMessage()

  fun showPostAlreadyRatedMessage()

  fun showUnknownErrorMessage()

  fun blockScreenSleep()

  fun unblockScreenSleep()

  fun showFab()

  fun hideFab()
}
