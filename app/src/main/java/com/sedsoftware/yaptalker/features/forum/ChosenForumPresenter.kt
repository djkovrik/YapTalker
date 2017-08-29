package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Topic
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
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

  @Inject
  lateinit var titleChannel: BehaviorRelay<String>

  private var currentForumId = 0
  private var currentSorting = LAST_UPDATE_SORTER
  private var currentPage = 0
  private var totalPages = -1
  private var currentTitle = ""

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    attachRefreshIndicator(yapDataManager.requestState, {
      // onStart
      viewState.showRefreshing()
    }, {
      // onFinish
      viewState.hideRefreshing()
    })
  }

  fun checkSavedState(forumId: Int, savedViewState: Bundle?, key: String) {
    if (savedViewState != null && savedViewState.containsKey(key)) {
      val topics = savedViewState.getParcelableArrayList<Topic>(key)
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

  fun goToChosenPage() {
    viewState.showGoToPageDialog(totalPages)
  }

  fun loadForum(forumId: Int, page: Int = 0, sortByRank: Boolean = false) {
    currentForumId = forumId
    currentPage = page
    currentSorting =
        if (sortByRank) RATING_SORTER
        else LAST_UPDATE_SORTER

    loadForumCurrentPage()
  }

  fun loadChosenForumPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage - OFFSET_FOR_PAGE_NUMBER
      loadForumCurrentPage()
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  private fun loadForumCurrentPage() {

    val startingTopic = currentPage * TOPICS_PER_PAGE

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
        .apply { unsubscribeOnDestroy(this) }
  }

  private fun onLoadingSuccess(forumPage: ForumPage) {
    totalPages = forumPage.totalPages.toInt()
    currentTitle = forumPage.forumTitle
    viewState.refreshTopics(forumPage.topics)
    viewState.scrollToViewTop()
    viewState.setAppbarTitle(currentTitle)
    setNavigationLabel()
    setNavigationAvailability()
  }

  private fun onRestoringSuccess(topics: List<Topic>) {
    viewState.refreshTopics(topics)
    viewState.setAppbarTitle(currentTitle)
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

    var backNavigationAvailable = true
    var forwardNavigationAvailable = true

    when (currentPage) {
      0 -> {
        backNavigationAvailable = false
      }
      totalPages - OFFSET_FOR_PAGE_NUMBER -> {
        forwardNavigationAvailable = false
      }
    }

    viewState.setIfNavigationBackEnabled(backNavigationAvailable)
    viewState.setIfNavigationForwardEnabled(forwardNavigationAvailable)
  }

  fun setAppbarTitle(title: String) {
    pushAppbarTitle(titleChannel, title)
  }
}
