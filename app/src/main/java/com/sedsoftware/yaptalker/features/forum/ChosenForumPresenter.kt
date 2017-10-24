package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.model.ForumNavigationPanel
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Topic
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenForumPresenter : BasePresenter<ChosenForumView>() {

  companion object {
    private const val FORUM_PAGE_KEY = "FORUM_PAGE_KEY"
    private const val LAST_UPDATE_SORTER = "last_post"
    private const val RATING_SORTER = "rank"
    private const val TOPICS_PER_PAGE = 30
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var currentTitle = ""
  private var currentForumId = 0
  private var currentSorting = LAST_UPDATE_SORTER
  private var currentPage = 1
  private var totalPages = 1

  fun checkSavedState(forumId: Int, savedViewState: Bundle?) {
    if (savedViewState != null &&
        savedViewState.containsKey(FORUM_PAGE_KEY)) {

      with(savedViewState) {
        onRestoringSuccess(getParcelable(FORUM_PAGE_KEY))
      }
    } else {
      loadForum(forumId)
    }
  }

  fun saveCurrentState(outState: Bundle, currentForumId: Int, panel: ForumNavigationPanel, topics: List<Topic>) {
    val forumPage = ForumPage(
        title = currentTitle,
        id = currentForumId.toString(),
        navigationPanel = panel,
        topicsList = topics)

    with(outState) {
      putParcelable(FORUM_PAGE_KEY, forumPage)
    }
  }

  fun loadForum(forumId: Int, page: Int = 1, sortByRank: Boolean = false) {
    currentForumId = forumId
    currentPage = page
    currentSorting =
        if (sortByRank) RATING_SORTER
        else LAST_UPDATE_SORTER

    loadForumCurrentPage()
  }

  fun goToFirstPage() {
    currentPage = 1
    loadForumCurrentPage()
  }

  fun goToLastPage() {
    currentPage = totalPages
    loadForumCurrentPage()
  }

  fun goToPreviousPage() {
    currentPage--
    loadForumCurrentPage()
  }

  fun goToNextPage() {
    currentPage++
    loadForumCurrentPage()
  }

  fun goToChosenPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage
      loadForumCurrentPage()
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  private fun loadForumCurrentPage() {

    val startingTopic = (currentPage - OFFSET_FOR_PAGE_NUMBER) * TOPICS_PER_PAGE

    yapDataManager
        .getChosenForum(currentForumId, startingTopic, currentSorting)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          forumPage ->
          onLoadingSuccess(forumPage)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onRestoringSuccess(page: ForumPage) {
    currentTitle = page.forumTitle
    currentPage = page.navigation.currentPage.toInt()
    totalPages = page.navigation.totalPages.toInt()
    updateAppbarTitle(currentTitle)
    viewState.displayForumPage(page)
  }

  private fun onLoadingSuccess(page: ForumPage) {
    onRestoringSuccess(page)
    viewState.scrollToViewTop()
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
