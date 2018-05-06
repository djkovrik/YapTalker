package com.sedsoftware.yaptalker.presentation.feature.bookmarks

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.BookmarksInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.extensions.extractYapIds
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter.BookmarkElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.BookmarksModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class BookmarksPresenter @Inject constructor(
  private val router: Router,
  private val bookmarksInteractor: BookmarksInteractor,
  private val bookmarksMapper: BookmarksModelMapper
) : BasePresenter<BookmarksView>(), BookmarkElementsClickListener {

  private var clearCurrentList = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadBookmarks()
  }

  override fun attachView(view: BookmarksView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  override fun onDeleteIconClick(bookmarkId: Int) {
    viewState.showDeleteConfirmationDialog(bookmarkId)
  }

  override fun onTopicItemClick(link: String) {
    val triple = link.extractYapIds()
    if (triple.first != 0) {
      router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
    }
  }

  fun loadBookmarks() {

    clearCurrentList = true

    bookmarksInteractor
      .getBookmarks()
      .subscribeOn(Schedulers.io())
      .map(bookmarksMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe { viewState.showLoadingIndicator() }
      .doFinally { viewState.hideLoadingIndicator() }
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getBookmarksObserver())
  }

  fun deleteSelectedBookmark(bookmarkId: Int) {
    bookmarksInteractor
      .deleteFromBookmarks(bookmarkId)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({
        Timber.i("Bookmark deletion completed.")
        viewState.showBookmarkDeletedMessage()
        loadBookmarks()
      }, { error ->
        error.message?.let { viewState.showErrorMessage(it) }
      })
  }

  private fun getBookmarksObserver() =
    object : DisposableObserver<BookmarkedTopicModel?>() {

      override fun onNext(item: BookmarkedTopicModel) {
        if (clearCurrentList) {
          clearCurrentList = false
          viewState.clearBookmarksList()
        }

        viewState.appendBookmarkItem(item)
      }

      override fun onComplete() {
        Timber.i("Bookmarks loading completed.")
      }

      override fun onError(e: Throwable) {
        e.message?.let { viewState.showErrorMessage(it) }
      }
    }
}
