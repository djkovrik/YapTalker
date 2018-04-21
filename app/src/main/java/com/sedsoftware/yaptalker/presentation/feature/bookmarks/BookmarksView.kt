package com.sedsoftware.yaptalker.presentation.feature.bookmarks

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface BookmarksView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendBookmarkItem(bookmark: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearBookmarksList()

  fun updateCurrentUiState()

  fun showDeleteConfirmationDialog(bookmarkId: Int)

  fun showBookmarkDeletedMessage()
}
