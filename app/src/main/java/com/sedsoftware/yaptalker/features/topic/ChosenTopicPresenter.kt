package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.commons.UpdateAppbarEvent
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.TopicPost
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.sedsoftware.yaptalker.features.base.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenTopicPresenter : BasePresenter<ChosenTopicView>() {

  companion object {
    private const val POSTS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var currentForumId = 0
  private var currentTopicId = 0
  private var currentPage = 0
  private var totalPages = -1
  private var currentTitle = ""
  private var authKey = ""

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    attachRefreshIndicator({
      // onStart
      viewState.showRefreshing()
    }, {
      // onFinish
      viewState.hideRefreshing()
    })
  }

  override fun attachView(view: ChosenTopicView?) {
    super.attachView(view)
    viewState.hideNavigationPanel()
    viewState.hideFabWithoutAnimation()
  }

  fun checkSavedState(forumId: Int, topicId: Int, savedViewState: Bundle?, key: String) {
    if (savedViewState != null && savedViewState.containsKey(key)) {
      val posts = savedViewState.getParcelableArrayList<TopicPost>(key)
      onRestoringSuccess(posts)
    } else {
      loadTopic(forumId, topicId)
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

  fun setAppbarTitle(title: String) {
    pushAppEvent(UpdateAppbarEvent(title))
  }

  // TODO() Show fab for authorized user only
  fun handleNavigationVisibility(isFabShown: Boolean, isNavigationShown: Boolean, diff: Int) {
    when {
      isNavigationShown && diff > 0 -> viewState.hideNavigationPanel()
      !isNavigationShown && diff < 0 -> viewState.showNavigationPanel()
      isFabShown && diff < 0 -> viewState.hideFab()
      !isFabShown && diff > 0 -> viewState.showFab()
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
    currentTitle = topicPage.topicTitle
    authKey = topicPage.authKey
    viewState.refreshPosts(topicPage.posts)
    viewState.scrollToViewTop()
    viewState.setAppbarTitle(currentTitle)
    setNavigationLabel()
    setNavigationAvailability()
  }

  private fun onRestoringSuccess(list: List<TopicPost>) {
    viewState.refreshPosts(list)
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
}
