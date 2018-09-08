package com.sedsoftware.yaptalker.presentation.feature.topic

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.BlacklistInteractor
import com.sedsoftware.yaptalker.domain.interactor.MessagePostingInteractor
import com.sedsoftware.yaptalker.domain.interactor.SiteKarmaInteractor
import com.sedsoftware.yaptalker.domain.interactor.TopicInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.feature.search.SearchRequest
import com.sedsoftware.yaptalker.presentation.feature.topic.adapter.ChosenTopicElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.EditedPostModelMapper
import com.sedsoftware.yaptalker.presentation.mapper.QuotedPostModelMapper
import com.sedsoftware.yaptalker.presentation.mapper.ServerResponseModelMapper
import com.sedsoftware.yaptalker.presentation.mapper.TopicModelMapper
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.EditedPostModel
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.sedsoftware.yaptalker.presentation.model.base.QuotedPostModel
import com.sedsoftware.yaptalker.presentation.model.base.ServerResponseModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostModel
import com.sedsoftware.yaptalker.presentation.model.base.TagModel
import com.sedsoftware.yaptalker.presentation.model.base.TopicInfoBlockModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@Suppress("LargeClass", "TooManyFunctions", "UNCHECKED_CAST")
@InjectViewState
class ChosenTopicPresenter @Inject constructor(
    private val router: Router,
    private val settings: Settings,
    private val topicInteractor: TopicInteractor,
    private val siteKarmaInteractor: SiteKarmaInteractor,
    private val messagePostingInteractor: MessagePostingInteractor,
    private val videoThumbnailsInteractor: VideoThumbnailsInteractor,
    private val blacklistInteractor: BlacklistInteractor,
    private val topicMapper: TopicModelMapper,
    private val quoteDataMapper: QuotedPostModelMapper,
    private val editedTextDataMapper: EditedPostModelMapper,
    private val serverResponseMapper: ServerResponseModelMapper,
    private val schedulers: SchedulersProvider
) : BasePresenter<ChosenTopicView>(), ChosenTopicElementsClickListener, NavigationPanelClickListener {

    companion object {
        private const val OFFSET_FOR_PAGE_NUMBER = 1
        private const val KARMA_SUCCESS_MARKER = "\"status\":1"
        private const val KARMA_ALREADY_CHANGED_MARKER = "\"status\":-1"
    }

    private val isScreenAlwaysActive: Boolean by lazy {
        settings.isScreenAlwaysOnEnabled()
    }

    private val postsPerPage: Int by lazy {
        settings.getMessagesPerPage()
    }

    var currentForumId = 0
    var currentTopicId = 0
    var currentPage = 1
    var totalPages = 1
    private var currentEditedPost = 0
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
        router.setResultListener(
            RequestCode.MESSAGE_TEXT
        ) { message -> sendMessage(message as Pair<String, String>) }
        router.setResultListener(RequestCode.EDITED_MESSAGE_TEXT) { message -> sendEditedMessage(message as String) }
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
        router.removeResultListener(RequestCode.MESSAGE_TEXT)
        router.removeResultListener(RequestCode.EDITED_MESSAGE_TEXT)
        super.onDestroy()
    }

    override fun onPostItemClicked(postId: Int, isKarmaAvailable: Boolean) {

        if (postId == 0 || authKey.isEmpty()) {
            return
        }

        if (isKarmaAvailable) {
            viewState.showPostKarmaMenu(postId)
        }
    }

    override fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean) {
        when {
            isVideo && url.contains("youtube") -> {
                val videoId = url.extractYoutubeVideoId()
                viewState.browseExternalResource("http://www.youtube.com/watch?v=$videoId")
            }

            isVideo && url.contains("coub") && settings.isExternalCoubPlayer() -> {
                viewState.browseExternalResource(url.validateUrl())
            }

            isVideo && !url.contains("youtube") -> {
                router.navigateTo(NavigationScreen.VIDEO_DISPLAY_SCREEN, html)
            }

            else -> {
                router.navigateTo(
                    NavigationScreen.TOPIC_GALLERY,
                    GalleryInitialState(currentForumId, currentTopicId, currentPage, url)
                )
            }
        }
    }

    override fun onUserAvatarClicked(userId: Int) {
        if (authKey.isNotEmpty()) {
            viewState.showUserProfile(userId)
        }
    }

    override fun onReplyButtonClicked(authorNickname: String, postDate: String, postId: Int) {
        topicInteractor
            .requestPostTextAsQuote(currentForumId, currentTopicId, postId)
            .map(quoteDataMapper)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ post ->
                post as QuotedPostModel
                val quote = "[QUOTE=$authorNickname,$postDate]${post.text}[/QUOTE]\n"
                router.navigateTo(NavigationScreen.MESSAGE_EDITOR_SCREEN, Triple(currentTitle, quote, ""))
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }

    override fun onEditButtonClicked(postId: Int) {
        val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage
        currentEditedPost = postId

        topicInteractor
            .requestPostTextForEditing(currentForumId, currentTopicId, postId, startingPost)
            .map(editedTextDataMapper)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ post ->
                post as EditedPostModel
                router.navigateTo(NavigationScreen.MESSAGE_EDITOR_SCREEN, Triple(currentTitle, "", post.text))
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }

    override fun onTopicTagClicked(tag: TagModel) {
        router.navigateTo(
            NavigationScreen.SEARCH_RESULTS, SearchRequest(searchFor = tag.searchParameter, searchInTags = true)
        )
    }

    override fun goToFirstPage() {
        currentPage = 1
        loadTopicCurrentPage(shouldScrollToViewTop = true)
    }

    override fun goToLastPage() {
        currentPage = totalPages
        loadTopicCurrentPage(shouldScrollToViewTop = true)
    }

    override fun goToPreviousPage() {
        currentPage--
        loadTopicCurrentPage(shouldScrollToViewTop = true)
    }

    override fun goToNextPage() {
        currentPage++
        loadTopicCurrentPage(shouldScrollToViewTop = true)
    }

    override fun goToSelectedPage() {
        viewState.showPageSelectionDialog()
    }

    fun goToChosenPage(chosenPage: Int) {
        if (chosenPage in 1..totalPages) {
            currentPage = chosenPage
            loadTopicCurrentPage(shouldScrollToViewTop = true)
        } else {
            viewState.showCantLoadPageMessage(chosenPage)
        }
    }

    fun loadTopic(forumId: Int, topicId: Int, startingPost: Int = 0) {
        currentForumId = forumId
        currentTopicId = topicId

        currentPage = when {
            startingPost != 0 -> startingPost / postsPerPage + 1
            else -> 1
        }

        loadTopicCurrentPage(shouldScrollToViewTop = true)
    }

    fun loadRestoredTopic(forumId: Int, topicId: Int, page: Int) {
        currentForumId = forumId
        currentTopicId = topicId
        currentPage = page

        loadTopicCurrentPage(shouldScrollToViewTop = false, restoreScrollState = true)
    }

    fun refreshCurrentPage() {
        loadTopicCurrentPage(shouldScrollToViewTop = false)
    }

    fun navigateToUserProfile(userId: Int) {
        router.navigateTo(NavigationScreen.USER_PROFILE_SCREEN, userId)
    }

    fun openTopicGallery() {
        viewState.hideLoadingIndicator()
        router.navigateTo(
            NavigationScreen.TOPIC_GALLERY,
            GalleryInitialState(currentForumId, currentTopicId)
        )
    }

    fun requestTopicBlacklist() {
        viewState.showBlacklistRequest()
    }

    fun navigateToMessagePostingScreen() {
        router.navigateTo(NavigationScreen.MESSAGE_EDITOR_SCREEN, Triple(currentTitle, "", ""))
    }

    fun shareCurrentTopic() {
        val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage
        viewState.shareTopic(currentTitle, startingPost)
    }

    fun showTopicKarmaMenuIfAvailable() {
        if (ratingTargetId == 0 || authKey.isEmpty()) {
            return
        }

        viewState.showTopicKarmaMenu()
    }

    fun handleFabVisibility(diff: Int) {
        when {
            diff > 0 -> viewState.hideFab()
            diff < 0 -> viewState.showFab()
        }
    }

    fun addCurrentTopicToBookmarks() {

        val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage

        topicInteractor
            .requestBookmarkAdding(currentTopicId, startingPost)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Current topic added to bookmarks.")
                viewState.showBookmarkAddedMessage()
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }

    fun addCurrentTopicToBlacklist() {
        blacklistInteractor
            .addTopicToBlacklist(currentTitle, currentTopicId)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Current topic added to blacklist.")
                viewState.showTopicBlacklistedMessage()
                router.exitWithResult(RequestCode.REFRESH_REQUEST, true)
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }

    fun changeKarma(postId: Int = 0, isTopic: Boolean, shouldIncrease: Boolean) {

        if (authKey.isEmpty() || currentTopicId == 0) {
            return
        }

        Timber.d("changeKarma(postId = $postId, isTopic = $isTopic, shouldIncrease = $shouldIncrease")

        val targetPostId = if (isTopic || postId == 0) ratingTargetId else postId
        val diff = if (shouldIncrease) 1 else -1

        siteKarmaInteractor
            .sendChangeKarmaRequest(isTopic, targetPostId, currentTopicId, diff)
            .map(serverResponseMapper)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getKarmaResponseObserver(isTopic, targetPostId, shouldIncrease))
    }

    fun requestThumbnail(videoUrl: String): Single<String> =
        videoThumbnailsInteractor
            .getThumbnail(videoUrl)
            .observeOn(schedulers.ui())

    private fun sendMessage(message: Pair<String, String>) {

        if (authKey.isEmpty()) {
            return
        }

        val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage

        messagePostingInteractor
            .sendMessageRequest(currentForumId, currentTopicId, startingPost, authKey, message.first, message.second)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Send message request completed.")
                refreshCurrentPage()
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }

    private fun sendEditedMessage(message: String) {

        if (authKey.isEmpty() || currentEditedPost == 0) {
            return
        }

        val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage

        messagePostingInteractor
            .sendEditedMessageRequest(
                currentForumId,
                currentTopicId,
                currentEditedPost,
                startingPost,
                authKey,
                message
            )
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Send edited message request completed.")
                refreshCurrentPage()
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }

    private fun loadTopicCurrentPage(shouldScrollToViewTop: Boolean, restoreScrollState: Boolean = false) {

        if (!shouldScrollToViewTop) {
            viewState.saveScrollPosition()
        }

        val startingPost = (currentPage - OFFSET_FOR_PAGE_NUMBER) * postsPerPage
        clearCurrentList = true

        topicInteractor
            .getChosenTopic(currentForumId, currentTopicId, startingPost)
            .map(topicMapper)
            .flatMapObservable { Observable.fromIterable(it) }
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getTopicObserver(shouldScrollToViewTop, restoreScrollState))
    }

    // ==== OBSERVERS ====

    private fun getKarmaResponseObserver(isTopic: Boolean, targetPostId: Int, shouldIncrease: Boolean) =
        object : SingleObserver<ServerResponseModel?> {

            override fun onSubscribe(d: Disposable) {
            }

            override fun onSuccess(response: ServerResponseModel) {
                when {
                    response.text.contains(KARMA_SUCCESS_MARKER) -> viewState.showPostKarmaChangedMessage(isTopic)
                    response.text.contains(KARMA_ALREADY_CHANGED_MARKER) -> viewState.showPostAlreadyRatedMessage(
                        isTopic
                    )
                }

                Timber.i("Karma changing request completed.")
                if (!isTopic) {
                    viewState.updateKarmaUi(targetPostId, shouldIncrease)
                }
            }

            override fun onError(error: Throwable) {
                error.message?.let { viewState.showErrorMessage(it) }
            }
        }

    private fun getTopicObserver(scrollToViewTop: Boolean, restoreScrollState: Boolean) =
        object : DisposableObserver<DisplayedItemModel?>() {

            override fun onNext(item: DisplayedItemModel) {
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
                setupCurrentLoginSessionState()

                when {
                    scrollToViewTop -> viewState.scrollToViewTop()
                    restoreScrollState -> viewState.restoreScrollState()
                    else -> viewState.restoreScrollPosition()
                }
            }

            override fun onError(error: Throwable) {
                error.message?.let { viewState.showErrorMessage(it) }
            }
        }

    // ==== UTILITY ====

    private fun setupCurrentLoginSessionState() {
        val loggedIn = authKey.isNotEmpty()
        val karmaAvailable = ratingPlusAvailable && ratingMinusAvailable
        viewState.setLoggedInState(loggedIn)
        viewState.setTopicKarmaState(karmaAvailable)
    }
}
