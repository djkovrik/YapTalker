package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.parsing.ForumNavigationPanel
import com.sedsoftware.yaptalker.data.parsing.ForumPage
import com.sedsoftware.yaptalker.data.parsing.Topic
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenForumPresenter : BasePresenter<ChosenForumView>() {

  companion object {
    private const val FORUM_PAGE_KEY = "FORUM_PAGE_KEY"
    private const val LAST_UPDATE_SORTER = "last_post"
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
        onLoadingSuccess(page = getParcelable(FORUM_PAGE_KEY), scrollToTop = false)
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

  fun loadForum(forumId: Int) {
    currentForumId = forumId
    currentPage = 1

    loadForumCurrentPage(scrollToTop = false)
  }

  fun goToFirstPage() {
    currentPage = 1
    loadForumCurrentPage(scrollToTop = true)
  }

  fun goToLastPage() {
    currentPage = totalPages
    loadForumCurrentPage(scrollToTop = true)
  }

  fun goToPreviousPage() {
    currentPage--
    loadForumCurrentPage(scrollToTop = true)
  }

  fun goToNextPage() {
    currentPage++
    loadForumCurrentPage(scrollToTop = true)
  }

  fun goToChosenPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage
      loadForumCurrentPage(scrollToTop = true)
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  private fun loadForumCurrentPage(scrollToTop: Boolean) {

    val startingTopic = (currentPage - OFFSET_FOR_PAGE_NUMBER) * TOPICS_PER_PAGE

    yapDataManager
        .getChosenForum(currentForumId, startingTopic, currentSorting)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          forumPage ->
          onLoadingSuccess(forumPage, scrollToTop)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onLoadingSuccess(page: ForumPage, scrollToTop: Boolean) {

    currentTitle = page.forumTitle
    updateAppbarTitle(currentTitle)

    val pageString = page.navigation.currentPage
    val totalPageString = page.navigation.totalPages

    if (pageString.isNotEmpty() && totalPageString.isNotEmpty()) {
      currentPage = pageString.toInt()
      totalPages = totalPageString.toInt()
    }

    viewState.displayForumPage(page)

    if (scrollToTop) {
      viewState.scrollToViewTop()
    }
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
  }
}
