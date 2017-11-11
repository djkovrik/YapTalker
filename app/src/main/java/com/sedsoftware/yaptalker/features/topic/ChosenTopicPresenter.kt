package com.sedsoftware.yaptalker.features.topic

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.sedsoftware.yaptalker.base.navigation.RequestCodes
import com.sedsoftware.yaptalker.data.parsing.TopicPage
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

@Suppress("TooManyFunctions")
@InjectViewState
class ChosenTopicPresenter : BasePresenter<ChosenTopicView>() {

  init {
    router.setResultListener(RequestCodes.MESSAGE_TEXT, { message -> sendMessage(message as String) })
  }

  companion object {
    private const val POSTS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
    private const val BOOKMARK_SUCCESS_MARKER = "Закладка добавлена"
  }

  private var currentForumId = 0
  private var currentTopicId = 0
  private var currentPage = 1
  private var totalPages = 1
  private var rating = ""
  private var ratingPlusAvailable = ""
  private var ratingMinusAvailable = ""
  private var ratingPlusClicked = ""
  private var ratingMinusClicked = ""
  private var ratingTargetId = ""
  private var authKey = ""
  private var isClosed = ""
  private var currentTitle = ""

  override fun onDestroy() {
    super.onDestroy()
    router.removeResultListener(RequestCodes.MESSAGE_TEXT)
  }

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    viewState.initiateTopicLoading()
  }

  fun loadTopic(forumId: Int, topicId: Int, startingPost: Int = 0) {
    currentForumId = forumId
    currentTopicId = topicId

    currentPage = when {
      startingPost != 0 -> startingPost / POSTS_PER_PAGE + 1
      else -> 1
    }

    loadTopicCurrentPage(scrollToTop = false)
  }

  fun refreshCurrentPage() {
    loadTopicCurrentPage(scrollToTop = false)
  }

  fun goToFirstPage() {
    currentPage = 1
    loadTopicCurrentPage(scrollToTop = true)
  }

  fun goToLastPage() {
    currentPage = totalPages
    loadTopicCurrentPage(scrollToTop = true)
  }

  fun goToPreviousPage() {
    currentPage--
    loadTopicCurrentPage(scrollToTop = true)
  }

  fun goToNextPage() {
    currentPage++
    loadTopicCurrentPage(scrollToTop = true)
  }

  fun goToChosenPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage
      loadTopicCurrentPage(scrollToTop = true)
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  fun navigateToUserProfile(userId: Int) {
    router.navigateTo(NavigationScreens.USER_PROFILE_SCREEN, userId)
  }

  fun onNewMessageItemClicked() {
    router.navigateTo(NavigationScreens.ADD_MESSAGE_SCREEN, currentTitle)
  }

  fun onUserProfileClicked(userId: Int) {
    if (authKey.isNotEmpty()) {
      viewState.showUserProfile(userId)
    }
  }

  fun onShareItemClicked() {
    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE
    viewState.shareTopic(currentTitle, startingPost)
  }

  fun onBookmarkItemClicked() {
    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE

    yapDataManager
        .addTopicToBookmarks(currentTopicId, startingPost)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // On Success
          response ->
          onBookmarkResponseReceived(response)
        }, {
          // On Error
          error ->
          onLoadingError(error)
        })
  }

  fun onChangeTopicKarmaItemClicked(increaseKarma: Boolean) {

    if (ratingTargetId.isEmpty() || authKey.isEmpty() || currentTopicId == 0) {
      return
    }

    val diff = if (increaseKarma) 1 else -1

    yapDataManager
        .changeKarma(
            rank = diff,
            postId = ratingTargetId.toInt(),
            topicId = currentTopicId,
            type = 1)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // On Success
          response ->
          onKarmaResponseReceived(response)
          loadTopicCurrentPage(scrollToTop = false)
        }, {
          // On Error
          error ->
          onLoadingError(error)
        })
  }

  fun onChangePostKarmaItemClicked(postId: String, increaseKarma: Boolean) {

    if (postId.isEmpty() || authKey.isEmpty() || currentTopicId == 0) {
      return
    }

    val diff = if (increaseKarma) 1 else -1

    yapDataManager
        .changeKarma(
            rank = diff,
            postId = postId.toInt(),
            topicId = currentTopicId,
            type = 0)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // On Success
          response ->
          onKarmaResponseReceived(response)
          loadTopicCurrentPage(scrollToTop = false)
        }, {
          // On Error
          error ->
          onLoadingError(error)
        })
  }

  fun checkIfPostContextMenuAvailable(postId: String) {
    if (postId.isEmpty() || authKey.isEmpty()) {
      return
    }

    viewState.displayPostContextMenu(postId)
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

  private fun loadTopicCurrentPage(scrollToTop: Boolean) {

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE

    yapDataManager
        .getChosenTopic(currentForumId, currentTopicId, startingPost)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          page: TopicPage ->
          onLoadingSuccess(page, scrollToTop)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onLoadingSuccess(page: TopicPage, scrollToTop: Boolean) {
    rating = page.topicRating
    ratingPlusAvailable = page.topicRatingPlusAvailable
    ratingMinusAvailable = page.topicRatingMinusAvailable
    ratingPlusClicked = page.topicRatingPlusClicked
    ratingMinusClicked = page.topicRatingMinusClicked
    ratingTargetId = page.topicRatingTargetId
    authKey = page.authKey
    isClosed = page.isClosed
    currentTitle = page.topicTitle

    setupMenuButtons()
    viewState.updateAppbarTitle(currentTitle)

    val pageString = page.navigation.currentPage
    val totalPageString = page.navigation.totalPages

    if (pageString.isNotEmpty() && totalPageString.isNotEmpty()) {
      currentPage = pageString.toInt()
      totalPages = totalPageString.toInt()
    }

    viewState.displayTopicPage(page)

    if (scrollToTop) {
      viewState.scrollToViewTop()
    }
  }

  private fun onPostSuccess() {
    loadTopicCurrentPage(scrollToTop = true)
  }

  private fun onKarmaResponseReceived(response: Response<ResponseBody>) {
    response.body()?.string()?.let { str ->
      Timber.d("Response from karma change request: $str")
    }
  }

  private fun onBookmarkResponseReceived(response: Response<ResponseBody>) {
    response.body()?.string()?.let { str ->
      if (str.contains(BOOKMARK_SUCCESS_MARKER)) {
        viewState.showBookmarkAddedMessage()
      } else {
        viewState.showUnknownErrorMessage()
      }
    }
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { message ->
      viewState.showErrorMessage(message)
    }
  }

  private fun setupMenuButtons() {
    val loggedIn = authKey.isNotEmpty()
    val karmaAvailable = ratingPlusAvailable.isNotEmpty() && ratingMinusAvailable.isNotEmpty()
    viewState.setLoggedInState(loggedIn)
    viewState.setTopicKarmaState(karmaAvailable)
  }
}
