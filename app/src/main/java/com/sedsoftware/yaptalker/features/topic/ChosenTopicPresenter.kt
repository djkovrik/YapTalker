package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.parsing.TopicNavigationPanel
import com.sedsoftware.yaptalker.data.parsing.TopicPage
import com.sedsoftware.yaptalker.data.parsing.TopicPost
import com.sedsoftware.yaptalker.features.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenTopicPresenter : BasePresenter<ChosenTopicView>() {

  init {
    router.setResultListener(ChosenTopicFragment.MESSAGE_TEXT_REQUEST, { message -> sendMessage(message as String) })
  }

  companion object {
    private const val TOPIC_PAGE_KEY = "TOPIC_PAGE_KEY"
    private const val POSTS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var currentTitle = ""
  private var currentForumId = 0
  private var currentTopicId = 0
  private var currentPage = 1
  private var totalPages = 1
  private var authKey = ""
  private var isClosed = ""

  override fun attachView(view: ChosenTopicView?) {
    super.attachView(view)
    viewState.hideFabWithoutAnimation()
  }

  override fun onDestroy() {
    super.onDestroy()
    router.removeResultListener(ChosenTopicFragment.MESSAGE_TEXT_REQUEST)
  }

  fun checkSavedState(forumId: Int, topicId: Int, startingPost: Int, savedViewState: Bundle?) {
    if (savedViewState != null &&
        savedViewState.containsKey(TOPIC_PAGE_KEY)) {

      with(savedViewState) {
        onLoadingSuccess(getParcelable(TOPIC_PAGE_KEY))
      }
    } else {
      loadTopic(forumId, topicId, startingPost)
    }
  }

  fun saveCurrentState(outState: Bundle, panel: TopicNavigationPanel, list: List<TopicPost>) {

    val topicPage = TopicPage(currentTitle, isClosed, authKey, panel, list)
    outState.putParcelable(TOPIC_PAGE_KEY, topicPage)
  }

  fun loadTopic(forumId: Int, topicId: Int, startingPost: Int = 0) {
    currentForumId = forumId
    currentTopicId = topicId

    currentPage = when {
      startingPost != 0 -> startingPost / POSTS_PER_PAGE + 1
      else -> 1
    }

    loadTopicCurrentPage()
  }

  fun goToFirstPage() {
    currentPage = 1
    loadTopicCurrentPage()
  }

  fun goToLastPage() {
    currentPage = totalPages
    loadTopicCurrentPage()
  }

  fun goToPreviousPage() {
    currentPage--
    loadTopicCurrentPage()
  }

  fun goToNextPage() {
    currentPage++
    loadTopicCurrentPage()
  }

  fun goToChosenPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage
      loadTopicCurrentPage()
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  fun handleFabVisibility(isFabShown: Boolean, diff: Int) {
    when {
      authKey.isEmpty() || isClosed.isNotEmpty() -> viewState.hideFabWithoutAnimation()
      isFabShown && diff < 0 -> viewState.showFab(shouldShow = false)
      !isFabShown && diff > 0 -> viewState.showFab(shouldShow = true)
    }
  }

  fun loadProfileIfAvailable(userId: Int) {
    if (authKey.isNotEmpty()) {
      viewState.showUserProfile(userId)
    }
  }

  fun onShareItemClicked() {
    viewState.shareTopic(currentTitle)
  }

  fun navigateToUserProfile(userId: Int) {
    router.navigateTo(NavigationScreens.USER_PROFILE_SCREEN, userId)
  }

  fun navigateToAddMessageView() {
    router.navigateTo(NavigationScreens.ADD_MESSAGE_SCREEN, currentTitle)
  }

  private fun onPostSuccess() {
    loadTopicCurrentPage()
  }

  private fun sendMessage(message: String) {

    if (authKey.isEmpty()) {
      return
    }

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE

    yapDataManager
        .sendMessageToSite(currentForumId, currentTopicId, startingPost, authKey, message)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          onPostSuccess()
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun loadTopicCurrentPage() {

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE

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

  private fun onLoadingSuccess(page: TopicPage) {
    authKey = page.authKey
    isClosed = page.isClosed
    currentTitle = page.topicTitle
    updateAppbarTitle(currentTitle)

    val pageString = page.navigation.currentPage
    val totalPageString = page.navigation.totalPages

    if (pageString.isNotEmpty() && totalPageString.isNotEmpty()) {
      currentPage = pageString.toInt()
      totalPages = totalPageString.toInt()
    }

    viewState.displayTopicPage(page)
    viewState.scrollToViewTop()
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
