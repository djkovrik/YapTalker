package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.model.TopicNavigationPanel
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.TopicPost
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

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

  override fun attachView(view: ChosenTopicView?) {
    super.attachView(view)
    viewState.hideFabWithoutAnimation()
  }

  fun checkSavedState(forumId: Int, topicId: Int, savedViewState: Bundle?) {
    if (savedViewState != null &&
        savedViewState.containsKey(TOPIC_PAGE_KEY)) {

      with(savedViewState) {
        onRestoringSuccess(getParcelable(TOPIC_PAGE_KEY))
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
      authKey.isEmpty() -> viewState.hideFabWithoutAnimation()
      isFabShown && diff < 0 -> viewState.showFab(shouldShow = false)
      !isFabShown && diff > 0 -> viewState.showFab(shouldShow = true)
    }
  }

  fun onFabClicked() {
    viewState.showAddMessageActivity(currentTitle)
  }

  fun loadProfileIfAvailable(userId: Int) {
    if (authKey.isNotEmpty()) {
      viewState.showUserProfile(userId)
    }
  }

  // TODO() Add closed topics detection
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

  private fun onRestoringSuccess(page: TopicPage) {
    currentTitle = page.topicTitle
    // TODO() Fix pages parsing for signed in user
//    currentPage = page.navigation.currentPage.toInt()
//    totalPages = page.navigation.totalPages.toInt()
    Timber.d("currentPage = ${page.navigation.currentPage}")
    Timber.d("totalPages = ${page.navigation.totalPages}")
    updateAppbarTitle(currentTitle)
    viewState.displayTopicPage(page)
  }

  private fun onLoadingSuccess(page: TopicPage) {
    onRestoringSuccess(page)
    viewState.scrollToViewTop()
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
