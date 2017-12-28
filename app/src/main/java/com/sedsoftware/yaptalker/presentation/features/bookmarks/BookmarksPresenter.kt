package com.sedsoftware.yaptalker.presentation.features.bookmarks

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetBookmarksList
import com.sedsoftware.yaptalker.domain.interactor.SendBookmarkDeleteRequest
import com.sedsoftware.yaptalker.domain.interactor.SendBookmarkDeleteRequest.Params
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mappers.BookmarksModelMapper
import com.sedsoftware.yaptalker.presentation.mappers.ServerResponseModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
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
    private val getBookmarksUseCase: GetBookmarksList,
    private val bookmarksMapper: BookmarksModelMapper,
    private val deleteBookmarkUseCase: SendBookmarkDeleteRequest,
    private val serverResponseMapper: ServerResponseModelMapper
) : BasePresenter<BookmarksView>() {

  private var clearCurrentList = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadBookmarks()
  }

  override fun attachView(view: BookmarksView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  fun navigateToBookmarkedTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun requestDeleteConfirmationDialog(bookmarkId: Int) {
    viewState.showDeleteConfirmationDialog(bookmarkId)
  }

  fun deleteSelectedBookmark(bookmarkId: Int) {
    deleteBookmark(bookmarkId)
  }

  fun loadBookmarks() {

    clearCurrentList = true

    getBookmarksUseCase
        .buildUseCaseObservable(Unit)
        .subscribeOn(Schedulers.io())
        .map { bookmarkItem: BaseEntity -> bookmarksMapper.transform(bookmarkItem) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showLoadingIndicator() }
        .doFinally { viewState.hideLoadingIndicator() }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getBookmarksObserver())
  }

  private fun getBookmarksObserver() =
      object : DisposableObserver<YapEntity?>() {

        override fun onNext(item: YapEntity) {
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

  private fun deleteBookmark(bookmarkId: Int) {
    deleteBookmarkUseCase
        .buildUseCaseObservable(Params(bookmarkId))
        .subscribeOn(Schedulers.io())
        .subscribeOn(Schedulers.io())
        .map { response: BaseEntity -> serverResponseMapper.transform(response) }
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getBookmarksResponseObserver())
  }

  private fun getBookmarksResponseObserver() =
      object : DisposableObserver<YapEntity?>() {

        override fun onNext(response: YapEntity) {
        }

        override fun onComplete() {
          Timber.i("Bookmark deletion completed.")
          viewState.showBookmarkDeletedMessage()
          loadBookmarks()
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }
}
