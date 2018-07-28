package com.sedsoftware.yaptalker.presentation.feature.bookmarks

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.interactor.BookmarksInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.extensions.extractYapIds
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter.BookmarksElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.BookmarksModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class BookmarksPresenter @Inject constructor(
    private val router: Router,
    private val bookmarksInteractor: BookmarksInteractor,
    private val bookmarksMapper: BookmarksModelMapper,
    private val schedulers: SchedulersProvider
) : BasePresenter<BookmarksView>(), BookmarksElementsClickListener {

    private var clearCurrentList = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBookmarks()
    }

    override fun attachView(view: BookmarksView?) {
        super.attachView(view)
        viewState.updateCurrentUiState()
    }

    override fun onDeleteIconClick(item: BookmarkedTopicModel) {
        viewState.showDeleteConfirmationDialog(item)
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
            .map(bookmarksMapper)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getBookmarksObserver())
    }

    fun deleteSelectedBookmark(item: BookmarkedTopicModel) {
        bookmarksInteractor
            .deleteFromBookmarks(item.bookmarkId)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Bookmark deletion completed.")
                viewState.showBookmarkDeletedMessage()
                loadBookmarks()
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
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
