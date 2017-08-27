package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ChosenTopicPresenter : BasePresenter<ChosenTopicView>() {

  companion object {
    private const val POSTS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  @Inject
  lateinit var yapDataManager: YapDataManager

  private var currentForumId = 0
  private var currentTopicId = 0
  private var currentPage = 0
  private var totalPages = -1

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

  private fun loadTopicCurrentPage() {

    val startingPost = currentPage * POSTS_PER_PAGE

    val subscription =
        yapDataManager
            .getChosenTopic(currentForumId, currentTopicId, startingPost)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              // onSuccess
              page: TopicPage ->
              onLoadingSuccess(page)
            }, {
              // onError
              throwable ->
              onLoadingError(throwable)
            })

    unsubscribeOnDestroy(subscription)
  }

  private fun onLoadingSuccess(topicPage: TopicPage) {
    totalPages = topicPage.totalPages.getLastDigits()
    viewState.refreshPosts(topicPage.posts)
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