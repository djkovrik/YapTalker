package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Topic
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.data.remote.yap.YapRequestState
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ChosenForumPresenter : BasePresenter<ChosenForumView>() {

  companion object {
    private const val LAST_UPDATE_SORTER = "last_post"
    private const val RATING_SORTER = "rank"
    private const val TOPICS_PER_PAGE = 30
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  @Inject
  lateinit var yapDataManager: YapDataManager

  private var currentForumId = 0
  private var currentSorting = LAST_UPDATE_SORTER
  private var currentPage = 0
  private var totalPages = -1

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    attachRefreshIndicator()
  }

  fun checkSavedState(forumId: Int, savedViewState: Bundle?) {
    if (savedViewState != null && savedViewState.containsKey(ChosenForumController.TOPICS_LIST_KEY)) {
      val topics = savedViewState.getParcelableArrayList<Topic>(ChosenForumController.TOPICS_LIST_KEY)
      onRestoringSuccess(topics)
    } else {
      loadForum(forumId)
    }
  }

  fun goToNextPage() {
    if (currentPage in 0 until totalPages - 1) {
      currentPage++
      loadForumCurrentPage()
    }
  }

  fun goToPreviousPage() {
    if (currentPage in 1 until totalPages) {
      currentPage--
      loadForumCurrentPage()
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

    val subscription =
        yapDataManager
            .getChosenForum(currentForumId, startingTopic, currentSorting)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              // onSuccess
              page: ForumPage ->
              onLoadingSuccess(page)
            }, {
              // onError
              throwable ->
              onLoadingError(throwable)
            })

    unsubscribeOnDestroy(subscription)
  }

  private fun attachRefreshIndicator() {

    val subscription =
        yapDataManager.requestState.subscribe { state: Long ->
          when (state) {
            YapRequestState.LOADING -> {
              viewState.setIfNavigationPanelVisible(false)
              viewState.showRefreshing()
            }
            YapRequestState.COMPLETED,
            YapRequestState.ERROR -> {
              viewState.hideRefreshing()
              viewState.setIfNavigationPanelVisible(true)
            }
          }
        }

    unsubscribeOnDestroy(subscription)
  }

  private fun onLoadingSuccess(forumPage: ForumPage) {
    totalPages = forumPage.totalPages.toInt()
    viewState.refreshTopics(forumPage.topics)
    setNavigationLabel()
    setNavigationAvailability()
  }

  private fun onRestoringSuccess(topics: List<Topic>) {
    viewState.refreshTopics(topics)
    setNavigationLabel()
    setNavigationAvailability()
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }

  private fun setNavigationLabel() {
    viewState.setNavigationPagesLabel(currentPage + OFFSET_FOR_PAGE_NUMBER, totalPages)
  }

  private fun setNavigationAvailability() {
    when (currentPage) {
      0 -> viewState.setIfNavigationBackEnabled(false)
      totalPages -> viewState.setIfNavigationForwardEnabled(false)
      else -> {
        viewState.setIfNavigationBackEnabled(true)
        viewState.setIfNavigationForwardEnabled(true)
      }
    }
  }
}
