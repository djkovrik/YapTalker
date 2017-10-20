package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.TopicPost
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenTopicPresenter : BasePresenter<ChosenTopicView>() {

  companion object {
    private const val POSTS_LIST_KEY = "POSTS_LIST_KEY"
    private const val CURRENT_TITLE_KEY = "CURRENT_TITLE_KEY"
    private const val POSTS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var currentTitle = ""
  private var currentForumId = 0
  private var currentTopicId = 0
  private var currentPage = 0
  private var totalPages = -1
  private var authKey = ""

  override fun attachView(view: ChosenTopicView?) {
    super.attachView(view)
    viewState.hideFabWithoutAnimation()
  }

  fun checkSavedState(forumId: Int, topicId: Int, savedViewState: Bundle?) {
    if (savedViewState != null &&
        savedViewState.containsKey(POSTS_LIST_KEY) &&
        savedViewState.containsKey(CURRENT_TITLE_KEY)) {

      with (savedViewState) {
        onRestoringSuccess(getParcelableArrayList(POSTS_LIST_KEY), getString(CURRENT_TITLE_KEY))
      }
    } else {
      loadTopic(forumId, topicId)
    }
  }

  fun saveCurrentState(outState: Bundle, posts: ArrayList<TopicPost>) {
    with(outState) {
      putParcelableArrayList(POSTS_LIST_KEY, posts)
      putString(CURRENT_TITLE_KEY, currentTitle)
    }
  }

  fun goToNextPage() {
    if (currentPage in 0 until totalPages - 1) {
      currentPage++
      loadTopicCurrentPage()
    }
  }

  fun goToPreviousPage() {
    if (currentPage in 1 until totalPages) {
      currentPage--
      loadTopicCurrentPage()
    }
  }

  fun goToChosenPage() {
    viewState.showGoToPageDialog(totalPages)
  }

  fun loadTopic(forumId: Int, topicId: Int, page: Int = 0) {
    currentForumId = forumId
    currentTopicId = topicId
    currentPage = page

    loadTopicCurrentPage()
  }

  fun loadChosenTopicPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage - OFFSET_FOR_PAGE_NUMBER
      loadTopicCurrentPage()
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  fun handleNavigationVisibility(isFabShown: Boolean, diff: Int) {
    when {
      authKey.isEmpty() -> viewState.hideFabWithoutAnimation()
      isFabShown && diff < 0 -> viewState.showFab(shouldShow = false)
      !isFabShown && diff > 0 -> viewState.showFab(shouldShow = true)
    }
  }

  fun onFabClicked() {
    viewState.showAddMessageActivity(currentTitle)
  }

  // TODO() Add closed topics detection
  fun sendMessage(message: String) {

    if (authKey.isEmpty()) {
      return
    }

    val topicPage = currentPage * POSTS_PER_PAGE

    yapDataManager
        .sendMessageToSite(currentForumId, currentTopicId, topicPage, authKey, message)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({ page ->
          // onSuccess
          onPostSuccess(page)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onPostSuccess(page: TopicPage) {
    loadTopicCurrentPage()
  }

  private fun loadTopicCurrentPage() {

    val startingPost = currentPage * POSTS_PER_PAGE

    yapDataManager
        .getChosenTopic(currentForumId, currentTopicId, startingPost)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          page: TopicPage ->
          onLoadingSuccess(page)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onLoadingSuccess(topicPage: TopicPage) {
    totalPages = topicPage.totalPages.getLastDigits()
    authKey = topicPage.authKey
    currentTitle = topicPage.topicTitle
    updateAppbarTitle(currentTitle)
    viewState.refreshPosts(topicPage.posts)
    viewState.scrollToViewTop()
    setNavigationLabel()
    setNavigationAvailability()
  }

  private fun onRestoringSuccess(list: List<TopicPost>, title: String) {
    viewState.refreshPosts(list)
    updateAppbarTitle(title)
    setNavigationLabel()
    setNavigationAvailability()
    updateAppbarTitle(currentTitle)
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
}
