package com.sedsoftware.yaptalker.presentation.feature.bookmarks

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel

@StateStrategyType(SkipStrategy::class)
interface BookmarksView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendBookmarkItem(item: BookmarkedTopicModel)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearBookmarksList()

  fun showDeleteConfirmationDialog(item: BookmarkedTopicModel)

  fun showBookmarkDeletedMessage()

  fun deleteItemFromBookmarks(item: BookmarkedTopicModel)
}
