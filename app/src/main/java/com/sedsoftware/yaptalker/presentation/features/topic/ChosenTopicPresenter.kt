package com.sedsoftware.yaptalker.presentation.features.topic

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetChosenTopic
import com.sedsoftware.yaptalker.domain.interactor.GetVideoThumbnail
import com.sedsoftware.yaptalker.domain.interactor.SendBookmarkAddRequest
import com.sedsoftware.yaptalker.domain.interactor.SendChangeKarmaRequestPost
import com.sedsoftware.yaptalker.domain.interactor.SendChangeKarmaRequestTopic
import com.sedsoftware.yaptalker.domain.interactor.SendMessageRequest
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.mappers.ServerResponseModelMapper
import com.sedsoftware.yaptalker.presentation.mappers.TopicModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.sedsoftware.yaptalker.presentation.model.base.ServerResponseModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostModel
import com.sedsoftware.yaptalker.presentation.model.base.TopicInfoBlockModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
@InjectViewState
class ChosenTopicPresenter @Inject constructor(
    private val router: Router,
    private val settings: SettingsManager,
    private val addToBookmarksUseCase: SendBookmarkAddRequest,
    private val changeTopicKarmaUseCase: SendChangeKarmaRequestTopic,
    private val changePostKarmaUseCase: SendChangeKarmaRequestPost,
    private val serverResponseMapper: ServerResponseModelMapper,
    private val sendMessageUseCase: SendMessageRequest,
    private val getChosenTopicUseCase: GetChosenTopic,
    private val topicMapper: TopicModelMapper,
    private val getVideoThumbnailUseCase: GetVideoThumbnail
) : BasePresenter<ChosenTopicView>() {

  companion object {
    private const val POSTS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
    private const val BOOKMARK_SUCCESS_MARKER = "Закладка добавлена"
  }

  private val isScreenAlwaysActive: Boolean by lazy {
    settings.isScreenAlwaysOnEnabled()
  }

  private var currentForumId = 0
  private var currentTopicId = 0
  private var currentPage = 1
  private var totalPages = 1
  private var rating = 0
  private var ratingPlusAvailable = false
  private var ratingMinusAvailable = false
  private var ratingPlusClicked = false
  private var ratingMinusClicked = false
  private var ratingTargetId = 0
  private var authKey = ""
  private var isClosed = false
  private var currentTitle = ""
  private var clearCurrentList = false

  init {
    router.setResultListener(RequestCode.MESSAGE_TEXT, { message -> sendMessage(message as String) })
  }

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    viewState.initiateTopicLoading()
  }

  override fun attachView(view: ChosenTopicView?) {
    super.attachView(view)

    if (isScreenAlwaysActive) {
      viewState.blockScreenSleep()
    }
  }

  override fun detachView(view: ChosenTopicView?) {
    if (isScreenAlwaysActive) {
      viewState.unblockScreenSleep()
    }

    super.detachView(view)
  }

  override fun onDestroy() {
    super.onDestroy()
    router.removeResultListener(RequestCode.MESSAGE_TEXT)
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

  fun refreshCurrentPage() {
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

  fun navigateToUserProfile(userId: Int) {
    router.navigateTo(NavigationScreen.USER_PROFILE_SCREEN, userId)
  }

  fun navigateToChosenVideo(html: String) {
    router.navigateTo(NavigationScreen.VIDEO_DISPLAY_SCREEN, html)
  }

  fun navigateToChosenImage(url: String) {
    router.navigateTo(NavigationScreen.IMAGE_DISPLAY_SCREEN, url)
  }

  fun navigateToChosenGif(url: String) {
    router.navigateTo(NavigationScreen.GIF_DISPLAY_SCREEN, url)
  }

  fun navigateToMessagePostingScreen() {
    router.navigateTo(NavigationScreen.ADD_MESSAGE_SCREEN, currentTitle)
  }

  fun onUserProfileClicked(userId: Int) {
    if (authKey.isNotEmpty()) {
      viewState.showUserProfile(userId)
    }
  }

  fun shareCurrentTopic() {
    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE
    viewState.shareTopic(currentTitle, startingPost)
  }

  fun showPostContextMenuIfAvailable(postId: Int) {
    if (postId == 0 || authKey.isEmpty()) {
      return
    }

    viewState.displayPostContextMenu(postId)
  }

  fun addCurrentTopicToBookmarks() {

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE

    addToBookmarksUseCase
        .buildUseCaseObservable(SendBookmarkAddRequest.Params(currentTopicId, startingPost))
        .subscribeOn(Schedulers.io())
        .map { response: BaseEntity -> serverResponseMapper.transform(response) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showLoadingIndicator() }
        .doFinally { viewState.hideLoadingIndicator() }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getBookmarksResponseObserver())
  }

  fun changeTopicKarma(shouldIncrease: Boolean) {

    if (authKey.isEmpty() || ratingTargetId == 0 || currentTopicId == 0) {
      return
    }

    val diff = if (shouldIncrease) 1 else -1

    changeTopicKarmaUseCase
        .buildUseCaseObservable(SendChangeKarmaRequestTopic.Params(ratingTargetId, currentTopicId, diff))
        .subscribeOn(Schedulers.io())
        .map { response: BaseEntity -> serverResponseMapper.transform(response) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showLoadingIndicator() }
        .doFinally { viewState.hideLoadingIndicator() }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getKarmaResponseObserver())
  }

  fun changePostKarma(postId: Int, shouldIncrease: Boolean) {
    if (authKey.isEmpty() || postId == 0 || currentTopicId == 0) {
      return
    }

    val diff = if (shouldIncrease) 1 else -1

    changePostKarmaUseCase
        .buildUseCaseObservable(SendChangeKarmaRequestPost.Params(postId, currentTopicId, diff))
        .subscribeOn(Schedulers.io())
        .map { response: BaseEntity -> serverResponseMapper.transform(response) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showLoadingIndicator() }
        .doFinally { viewState.hideLoadingIndicator() }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getKarmaResponseObserver())
  }

  fun requestThumbnail(videoUrl: String): Observable<String> =
      getVideoThumbnailUseCase
          .buildUseCaseObservable(GetVideoThumbnail.Params(videoUrl))

  private fun sendMessage(message: String) {

    if (authKey.isEmpty()) {
      return
    }

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE

    sendMessageUseCase
        .buildUseCaseObservable(
            SendMessageRequest.Params(currentForumId, currentTopicId, startingPost, authKey, message))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showLoadingIndicator() }
        .doFinally { viewState.hideLoadingIndicator() }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getMessageSendingObserver())
  }

  private fun loadTopicCurrentPage() {

    val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * POSTS_PER_PAGE
    clearCurrentList = true

    getChosenTopicUseCase
        .buildUseCaseObservable(GetChosenTopic.Params(currentForumId, currentTopicId, startingPost))
        .subscribeOn(Schedulers.io())
        .map { topicItems: List<BaseEntity> -> topicMapper.transform(topicItems) }
        .flatMap { items: List<YapEntity> -> Observable.fromIterable(items) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showLoadingIndicator() }
        .doFinally { viewState.hideLoadingIndicator() }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getTopicObserver())
  }


  // ==== OBSERVERS ====

  private fun getBookmarksResponseObserver() =
      object : DisposableObserver<YapEntity?>() {

        override fun onNext(response: YapEntity) {

          response as ServerResponseModel

          if (response.text.contains(BOOKMARK_SUCCESS_MARKER)) {
            viewState.showBookmarkAddedMessage()
          } else {
            viewState.showUnknownErrorMessage()
          }
        }

        override fun onComplete() {
          Timber.i("Current topic added to bookmarks.")
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun getKarmaResponseObserver() =
      object : DisposableObserver<YapEntity?>() {

        override fun onNext(response: YapEntity) {
          response as ServerResponseModel
          Timber.d("Response from karma change request: ${response.text}")
        }

        override fun onComplete() {
          Timber.i("Karma changing request completed.")
          loadTopicCurrentPage()
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun getMessageSendingObserver() =
      object : DisposableObserver<BaseEntity?>() {

        override fun onNext(t: BaseEntity) {
        }

        override fun onComplete() {
          Timber.i("Send message request completed.")
          loadTopicCurrentPage()
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun getTopicObserver() =
      object : DisposableObserver<YapEntity?>() {

        override fun onNext(item: YapEntity) {
          if (clearCurrentList) {
            clearCurrentList = false
            viewState.clearPostsList()
          }

          when (item) {
            is TopicInfoBlockModel -> {
              rating = item.topicRating
              ratingPlusAvailable = item.topicRatingPlusAvailable
              ratingMinusAvailable = item.topicRatingMinusAvailable
              ratingPlusClicked = item.topicRatingPlusClicked
              ratingMinusClicked = item.topicRatingMinusClicked
              ratingTargetId = item.topicRatingTargetId
              authKey = item.authKey
              isClosed = item.isClosed
              currentTitle = item.topicTitle
            }
            is NavigationPanelModel -> {
              currentPage = item.currentPage
              totalPages = item.totalPages
              viewState.appendPostItem(item)
            }
            is SinglePostModel -> {
              viewState.appendPostItem(item)
            }
          }
        }

        override fun onComplete() {
          Timber.i("Topic page loading completed.")
          viewState.updateCurrentUiState(currentTitle)
          viewState.scrollToViewTop()
          setupMenuButtons()
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }


  // ==== UTILITY ====

  private fun setupMenuButtons() {
    val loggedIn = authKey.isNotEmpty()
    val karmaAvailable = ratingPlusAvailable && ratingMinusAvailable
    viewState.setLoggedInState(loggedIn)
    viewState.setTopicKarmaState(karmaAvailable)
  }
}
