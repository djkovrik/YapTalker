package com.sedsoftware.yaptalker.features.bookmarks

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.parsing.Bookmarks
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class BookmarksPresenter : BasePresenter<BookmarksView>() {

  override fun attachView(view: BookmarksView?) {
    super.attachView(view)
    loadBookmarks()
  }

  fun navigateToBookmarkedTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun onDeleteIconClicked(bookmarkId: Int) {
    viewState.showDeleteConfirmDialog(bookmarkId)
  }

  fun onDeleteConfirmed(bookmarkId: Int) {
    deleteBookmark(bookmarkId)
  }

  private fun loadBookmarks() {
    yapDataManager
        .getBookmarks()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          bookmarks ->
          onLoadingSuccess(bookmarks)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun deleteBookmark(bookmarkId: Int) {
    yapDataManager
        .removeTopicFromBookmarks(bookmarkId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          _ ->
          viewState.showBookmarkDeletedMessage()
          loadBookmarks()
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onLoadingSuccess(bookmarks: Bookmarks) {
    viewState.displayBookmarks(bookmarks)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
