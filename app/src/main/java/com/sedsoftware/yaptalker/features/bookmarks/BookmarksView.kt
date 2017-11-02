package com.sedsoftware.yaptalker.features.bookmarks

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.Bookmarks

@StateStrategyType(AddToEndSingleStrategy::class)
interface BookmarksView : BaseView {

  fun displayBookmarks(bookmarks: Bookmarks)

  fun showDeleteConfirmDialog(bookmarkId: Int)

  fun showBookmarkDeletedMessage()
}
