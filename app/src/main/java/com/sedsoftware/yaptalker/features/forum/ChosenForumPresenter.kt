package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Topic
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenForumPresenter : BasePresenter<ChosenForumView>() {

  companion object {
    private const val TOPICS_LIST_KEY = "TOPICS_LIST_KEY"
    private const val CURRENT_TITLE_KEY = "CURRENT_TITLE_KEY"
    private const val LAST_UPDATE_SORTER = "last_post"
    private const val RATING_SORTER = "rank"
    private const val TOPICS_PER_PAGE = 30
  }

  private var currentTitle = ""
  private var currentForumId = 0
  private var currentSorting = LAST_UPDATE_SORTER
  private var currentPage = 0
  private var totalPages = -1

  fun checkSavedState(forumId: Int, savedViewState: Bundle?) {
    if (savedViewState != null &&
        savedViewState.containsKey(TOPICS_LIST_KEY) &&
        savedViewState.containsKey(CURRENT_TITLE_KEY)) {

      with(savedViewState) {
        onRestoringSuccess(getParcelableArrayList(TOPICS_LIST_KEY), getString(CURRENT_TITLE_KEY))
      }
    } else {
      loadForum(forumId)
    }
  }

  fun saveCurrentState(outState: Bundle, topics: ArrayList<Topic>) {
    with(outState) {
      putParcelableArrayList(TOPICS_LIST_KEY, topics)
      putString(CURRENT_TITLE_KEY, currentTitle)
    }
  }
  
  fun loadForum(forumId: Int, page: Int = 0, sortByRank: Boolean = false) {
    currentForumId = forumId
    currentPage = page
    currentSorting =
        if (sortByRank) RATING_SORTER
        else LAST_UPDATE_SORTER

    loadForumCurrentPage()
  }

  private fun loadForumCurrentPage() {

    val startingTopic = currentPage * TOPICS_PER_PAGE

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


  private fun onLoadingSuccess(forumPage: ForumPage) {
    totalPages = forumPage.totalPages.toInt()
    currentTitle = forumPage.forumTitle
    updateAppbarTitle(currentTitle)

    viewState.refreshTopics(forumPage.topics)
    viewState.scrollToViewTop()
  }

  private fun onRestoringSuccess(topics: List<Topic>, title: String) {
    viewState.refreshTopics(topics)
    updateAppbarTitle(title)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
