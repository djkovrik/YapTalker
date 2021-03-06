package com.sedsoftware.yaptalker.presentation.feature.topic

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.device.storage.state.TopicState
import com.sedsoftware.yaptalker.device.storage.state.TopicStateStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.moveWithAnimationAxisY
import com.sedsoftware.yaptalker.presentation.extensions.orZero
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.feature.topic.adapter.ChosenTopicAdapter
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabMenu
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabMenuItemPrimary
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabMenuItemSecondary
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabOverlay
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_chosen_topic.topic_posts_list
import kotlinx.android.synthetic.main.fragment_chosen_topic.topic_refresh_layout
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_blacklist
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_blacklist_block
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_bookmark
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_bookmark_block
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_gallery
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_gallery_block
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_karma
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_karma_block
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_main_button_block
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_menu
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_new_message
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_new_message_label
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_overlay
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_refresh
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_refresh_block
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_share
import kotlinx.android.synthetic.main.include_topic_fab_menu.fab_share_block
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.share
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@Suppress("LargeClass", "TooManyFunctions")
@LayoutResource(value = R.layout.fragment_chosen_topic)
class ChosenTopicFragment : BaseFragment(), ChosenTopicView, ThumbnailsProvider {

    companion object {
        fun getNewInstance(triple: Triple<Int, Int, Int>): ChosenTopicFragment =
            ChosenTopicFragment().apply {
                arguments = bundleOf(
                    FORUM_ID_KEY to triple.first,
                    TOPIC_ID_KEY to triple.second,
                    STARTING_POST_KEY to triple.third
                )
            }

        fun getNewInstance(state: TopicState?): ChosenTopicFragment =
            ChosenTopicFragment().apply {
                arguments = bundleOf(
                    FORUM_ID_KEY to state?.forumId,
                    TOPIC_ID_KEY to state?.topicId,
                    STARTING_POST_KEY to state?.currentPage,
                    SAVED_SCROLL_STATE to state?.scrollState
                )
            }

        private const val FORUM_ID_KEY = "FORUM_ID_KEY"
        private const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
        private const val STARTING_POST_KEY = "SAVED_SCROLL_STATE"
        private const val SAVED_SCROLL_STATE = "STARTING_POST_KEY"
    }

    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var topicStateStorage: TopicStateStorage

    @Inject
    lateinit var topicAdapter: ChosenTopicAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: ChosenTopicPresenter

    @ProvidePresenter
    fun provideTopicPresenter() = presenter

    private val forumId: Int by lazy {
        arguments?.getInt(FORUM_ID_KEY).orZero()
    }

    private val topicId: Int by lazy {
        arguments?.getInt(TOPIC_ID_KEY).orZero()
    }

    private val startingPost: Int by lazy {
        arguments?.getInt(STARTING_POST_KEY).orZero()
    }

    private val savedScrollState: LinearLayoutManager.SavedState? by lazy {
        arguments?.getParcelable<LinearLayoutManager.SavedState>(SAVED_SCROLL_STATE)
    }

    private lateinit var topicScrollState: Parcelable

    private var fabMenu = FabMenu(isMenuExpanded = false)
    private var isLoggedIn = false
    private var isKarmaAvailable = false
    private var shouldSaveState = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(topic_posts_list) {
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            adapter = topicAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }

        topic_refresh_layout.setIndicatorColorScheme()

        topic_refresh_layout.setOnRefreshListener { presenter.refreshCurrentPage() }

        subscribeViews()

        topicScrollState = topic_posts_list.layoutManager?.onSaveInstanceState() ?: Bundle()
    }

    override fun onBackPressed(): Boolean {
        if (fabMenu.isMenuExpanded) {
            collapseMenu()
            return true
        }

        shouldSaveState = false
        return false
    }

    override fun onStop() {
        if (shouldSaveState) {
            val state = TopicState(
                forumId = presenter.currentForumId,
                topicId = presenter.currentTopicId,
                currentPage = presenter.currentPage,
                scrollState = topic_posts_list.layoutManager?.onSaveInstanceState() as LinearLayoutManager.SavedState
            )
            topicStateStorage.saveState(state)
        } else {
            topicStateStorage.clearState()
        }

        super.onStop()
    }

    override fun showErrorMessage(message: String) {
        messagesDelegate.showMessageError(message)
    }

    override fun showLoadingIndicator() {
        topic_refresh_layout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        topic_refresh_layout.isRefreshing = false
    }

    override fun appendPostItem(item: DisplayedItemModel) {
        topicAdapter.addPostItem(item)
    }

    override fun clearPostsList() {
        topicAdapter.clearPostsList()
    }

    override fun setLoggedInState(isLoggedIn: Boolean) {
        this.isLoggedIn = isLoggedIn
    }

    override fun setTopicKarmaState(isKarmaAvailable: Boolean) {
        this.isKarmaAvailable = isKarmaAvailable
    }

    override fun updateCurrentUiState(title: String) {
        setCurrentAppbarTitle(title)
        setCurrentNavDrawerItem(NavigationSection.FORUMS)
    }

    override fun initiateTopicLoading() {
        if (savedScrollState != null) {
            presenter.loadRestoredTopic(forumId, topicId, startingPost)
        } else {
            presenter.loadTopic(forumId, topicId, startingPost)
        }
    }

    override fun showUserProfile(userId: Int) {
        presenter.navigateToUserProfile(userId)
    }

    override fun shareTopic(title: String, topicPage: Int) {
        context?.share("http://www.yaplakal.com/forum$forumId/st/$topicPage/topic$topicId.html", title)
    }

    override fun showPostKarmaMenu(postId: Int) {
        val plusItem = context?.string(R.string.action_post_karma_plus)
        val minusItem = context?.string(R.string.action_post_karma_minus)

        val itemsArray = arrayListOf(plusItem, minusItem)

        context?.let { ctx ->
            MaterialDialog.Builder(ctx)
                .title(R.string.title_post_context_menu)
                .items(itemsArray)
                .itemsCallback { _, _, _, text ->
                    if (text == plusItem) presenter.changeKarma(postId, isTopic = false, shouldIncrease = true)
                    if (text == minusItem) presenter.changeKarma(postId, isTopic = false, shouldIncrease = false)
                }
                .show()
        }
    }

    override fun showTopicKarmaMenu() {
        val plusItem = context?.string(R.string.action_topic_karma_plus)
        val minusItem = context?.string(R.string.action_topic_karma_minus)

        val itemsArray = arrayListOf(plusItem, minusItem)

        context?.let { ctx ->
            MaterialDialog.Builder(ctx)
                .title(R.string.title_topic_karma_menu)
                .items(itemsArray)
                .itemsCallback { _, _, _, text ->
                    if (text == plusItem) {
                        collapseMenu()
                        presenter.changeKarma(isTopic = true, shouldIncrease = true)
                    }
                    if (text == minusItem) {
                        collapseMenu()
                        presenter.changeKarma(isTopic = true, shouldIncrease = false)
                    }
                }
                .show()
        }
    }

    override fun showBlacklistRequest() {
        context?.let { ctx ->
            MaterialDialog.Builder(ctx)
                .content(R.string.msg_blacklist_request)
                .positiveText(R.string.msg_blacklist_confirm_yes)
                .negativeText(R.string.msg_blacklist_confirm_no)
                .onPositive { _, _ -> presenter.addCurrentTopicToBlacklist() }
                .show()
        }
    }

    override fun saveScrollPosition() {
        topicScrollState = topic_posts_list.layoutManager?.onSaveInstanceState() ?: Bundle()
    }

    override fun restoreScrollPosition() {
        topic_posts_list.layoutManager?.onRestoreInstanceState(topicScrollState)
    }

    override fun restoreScrollState() {
        topic_posts_list.layoutManager?.onRestoreInstanceState(savedScrollState)
    }

    override fun scrollToViewTop() {
        topic_posts_list?.layoutManager?.scrollToPosition(0)
    }

    override fun showPageSelectionDialog() {
        view?.context?.let { ctx ->
            MaterialDialog.Builder(ctx)
                .title(R.string.navigation_go_to_page_title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(R.string.navigation_go_to_page_hint, 0, false) { _, input ->
                    presenter.goToChosenPage(input.toString().toInt())
                }
                .show()
        }
    }

    override fun showCantLoadPageMessage(page: Int) {
        context?.string(R.string.navigation_page_not_available)?.let { template ->
            messagesDelegate.showMessageWarning(String.format(Locale.getDefault(), template, page))
        }
    }

    override fun showBookmarkAddedMessage() {
        context?.string(R.string.msg_bookmark_topic_added)?.let { message ->
            messagesDelegate.showMessageSuccess(message)
        }
    }

    override fun showPostKarmaChangedMessage(isTopic: Boolean) {
        val message = if (isTopic) context?.string(R.string.msg_karma_changed_topic)
        else context?.string(R.string.msg_karma_changed_post)

        message?.let { text ->
            messagesDelegate.showMessageSuccess(text)
        }
    }

    override fun showPostAlreadyRatedMessage(isTopic: Boolean) {
        val message = if (isTopic) context?.string(R.string.msg_karma_already_rated_topic)
        else context?.string(R.string.msg_karma_already_rated_post)

        message?.let { text ->
            messagesDelegate.showMessageInfo(text)
        }
    }

    override fun showTopicBlacklistedMessage() {
        context?.string(R.string.msg_blacklist_added)?.let { message ->
            messagesDelegate.showMessageInfo(message)
        }
    }

    override fun showUnknownErrorMessage() {
        context?.string(R.string.msg_unknown_error)?.let { message ->
            messagesDelegate.showMessageError(message)
        }
    }

    override fun blockScreenSleep() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Timber.i("Screen always awake - enabled")
    }

    override fun unblockScreenSleep() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Timber.i("Screen always awake - disabled")
    }

    override fun showFab() {
        fab_main_button_block?.moveWithAnimationAxisY(offset = 0f)
    }

    override fun hideFab() {
        fab_main_button_block?.let { fab ->
            val offset = fab.height + fab.paddingTop + fab.paddingBottom
            fab.moveWithAnimationAxisY(offset = offset.toFloat())
        }
    }

    override fun updateKarmaUi(postId: Int, shouldIncrease: Boolean) {
        topicAdapter.updateKarmaUi(postId, shouldIncrease)
    }

    override fun loadThumbnail(videoUrl: String, imageView: ImageView) {
        presenter
            .requestThumbnail(videoUrl)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({ url ->
                if (url.isNotEmpty()) {
                    imageView.loadFromUrl(url)
                } else {
                    context?.let {
                        imageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.ic_othervideo
                            )
                        )
                    }
                }
            }, { e: Throwable ->
                Timber.e("Can't load image: ${e.message}")
            })
    }

    @SuppressLint("RxSubscribeOnError")
    private fun subscribeViews() {

//        RxSwipeRefreshLayout
//            .refreshes(topic_refresh_layout)
//            .autoDisposable(event(FragmentLifecycle.DESTROY))
//            .subscribe { presenter.refreshCurrentPage() }

        RxRecyclerView
            .scrollEvents(topic_posts_list)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { event -> presenter.handleFabVisibility(event.dy()) }

        RxView
            .clicks(fab_menu)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { initiateFabMenuDisplaying() }

        RxView
            .clicks(fab_refresh)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe {
                collapseMenu()
                presenter.refreshCurrentPage()
            }

        RxView
            .clicks(fab_bookmark)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe {
                collapseMenu()
                presenter.addCurrentTopicToBookmarks()
            }

        RxView
            .clicks(fab_share)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe {
                collapseMenu()
                presenter.shareCurrentTopic()
            }

        RxView
            .clicks(fab_gallery)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe {
                collapseMenu()
                presenter.openTopicGallery()
            }

        RxView
            .clicks(fab_blacklist)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe {
                collapseMenu()
                presenter.requestTopicBlacklist()
            }

        RxView
            .clicks(fab_karma)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { presenter.showTopicKarmaMenuIfAvailable() }

        RxView
            .clicks(fab_new_message)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe {
                collapseMenu()
                presenter.navigateToMessagePostingScreen()
            }

        RxView
            .clicks(fab_overlay)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { collapseMenu() }
    }

    private fun initiateFabMenuDisplaying() {
        if (fabMenu.isMenuExpanded) {
            collapseMenu()

        } else {
            refreshFabMenuState()
            expandMenu()
        }
    }

    private fun expandMenu() {
        fabMenu.showItems()
        topic_posts_list.isEnabled = !fabMenu.isMenuExpanded
    }

    private fun collapseMenu() {
        fabMenu.hideItems()
        topic_posts_list.isEnabled = !fabMenu.isMenuExpanded
    }

    private fun refreshFabMenuState() {

        fabMenu.clear()

        fabMenu.add(FabOverlay(fab_overlay))
        fabMenu.add(FabMenuItemPrimary(context, fab_menu, fab_new_message, fab_new_message_label, isLoggedIn))
        fabMenu.add(FabMenuItemSecondary(context, fab_refresh_block))

        if (isLoggedIn) {
            fabMenu.add(FabMenuItemSecondary(context, fab_bookmark_block))
        }

        if (isKarmaAvailable) {
            fabMenu.add(FabMenuItemSecondary(context, fab_karma_block))
        }

        fabMenu.add(FabMenuItemSecondary(context, fab_share_block))
        fabMenu.add(FabMenuItemSecondary(context, fab_gallery_block))
        fabMenu.add(FabMenuItemSecondary(context, fab_blacklist_block))
    }
}
