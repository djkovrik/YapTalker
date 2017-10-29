package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.model.TopicNavigationPanel
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.TopicPost
import com.sedsoftware.yaptalker.features.NavigationScreens
import com.sedsoftware.yaptalker.features.posting.AddMessageFragment
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenTopicPresenter : BasePresenter<ChosenTopicView>() {

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
  private var isClosed = false

  override fun attachView(view: ChosenTopicView?) {
    super.attachView(view)
    viewState.hideFabWithoutAnimation()
    router.setResultListener(AddMessageFragment.MESSAGE_TEXT_REQUEST, { message -> sendMessage(message as String) })
  }

  override fun detachView(view: ChosenTopicView?) {
    super.detachView(view)
    router.removeResultListener(AddMessageFragment.MESSAGE_TEXT_REQUEST)
  }

  fun checkSavedState(forumId: Int, topicId: Int, savedViewState: Bundle?) {
    if (savedViewState != null &&
        savedViewState.containsKey(TOPIC_PAGE_KEY)) {

      with(savedViewState) {
        onLoadingSuccess(getParcelable(TOPIC_PAGE_KEY))
      }
    } else {
      loadTopic(forumId, topicId)
    }
  }

  fun saveCurrentState(outState: Bundle, panel: TopicNavigationPanel, posts: List<TopicPost>) {
    val topicPage = TopicPage(
        title = currentTitle,
        navigationPanel = panel,
        key = authKey,
        postsList = posts)

    with(outState) {
      putParcelable(TOPIC_PAGE_KEY, topicPage)
    }
  }

  fun loadTopic(forumId: Int, topicId: Int, page: Int = 1) {
    currentForumId = forumId
    currentTopicId = topicId
    currentPage = page

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
      authKey.isEmpty() || isClosed -> viewState.hideFabWithoutAnimation()
      isFabShown && diff < 0 -> viewState.showFab(shouldShow = false)
      !isFabShown && diff > 0 -> viewState.showFab(shouldShow = true)
    }
  }

  fun onFabClicked() {
    viewState.showAddMessageView(currentTitle)
  }

  fun loadProfileIfAvailable(userId: Int) {
    if (authKey.isNotEmpty()) {
      viewState.showUserProfile(userId)
    }
  }

  fun sendMessage(message: String) {

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

  fun onShareItemClicked() {
    viewState.shareTopic(currentTitle)
  }

  fun navigateToUserProfile(userId: Int) {
    router.navigateTo(NavigationScreens.USER_PROFILE_SCREEN, userId)
  }

  fun navigateToAddMessageView(title: String) {
    router.navigateTo(NavigationScreens.ADD_MESSAGE_SCREEN, title)
  }

  private fun onPostSuccess() {
    loadTopicCurrentPage()
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
    isClosed = page.isClosed.isNotEmpty()
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
