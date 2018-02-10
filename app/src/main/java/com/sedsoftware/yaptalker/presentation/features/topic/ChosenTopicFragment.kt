package com.sedsoftware.yaptalker.presentation.features.topic

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.base.thumbnail.ThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.loadThumbnailFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.moveWithAnimationAxisY
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastInfo
import com.sedsoftware.yaptalker.presentation.extensions.toastSuccess
import com.sedsoftware.yaptalker.presentation.extensions.toastWarning
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicAdapter
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicElementsClickListener
import com.sedsoftware.yaptalker.presentation.features.topic.fabmenu.FabMenu
import com.sedsoftware.yaptalker.presentation.features.topic.fabmenu.FabMenuItemPrimary
import com.sedsoftware.yaptalker.presentation.features.topic.fabmenu.FabMenuItemSecondary
import com.sedsoftware.yaptalker.presentation.features.topic.fabmenu.FabOverlay
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chosen_topic.*
import kotlinx.android.synthetic.main.include_topic_fab_menu.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@Suppress("LargeClass", "TooManyFunctions")
@LayoutResource(value = R.layout.fragment_chosen_topic)
class ChosenTopicFragment : BaseFragment(), ChosenTopicView, ChosenTopicElementsClickListener,
  NavigationPanelClickListener, ThumbnailsLoader {

  companion object {
    fun getNewInstance(triple: Triple<Int, Int, Int>): ChosenTopicFragment {

      val fragment = ChosenTopicFragment()
      val args = Bundle()
      args.putInt(FORUM_ID_KEY, triple.first)
      args.putInt(TOPIC_ID_KEY, triple.second)
      args.putInt(STARTING_POST_KEY, triple.third)
      fragment.arguments = args
      return fragment
    }

    private const val FORUM_ID_KEY = "FORUM_ID_KEY"
    private const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
    private const val STARTING_POST_KEY = "STARTING_POST_KEY"
    private const val GIF_EXT = ".gif"
  }

  @Inject
  lateinit var topicAdapter: ChosenTopicAdapter

  @Inject
  @InjectPresenter
  lateinit var presenter: ChosenTopicPresenter

  @ProvidePresenter
  fun provideTopicPresenter() = presenter

  private val forumId: Int by lazy {
    arguments?.getInt(FORUM_ID_KEY) ?: 0
  }

  private val topicId: Int by lazy {
    arguments?.getInt(TOPIC_ID_KEY) ?: 0
  }

  private val startingPost: Int by lazy {
    arguments?.getInt(STARTING_POST_KEY) ?: 0
  }

  private lateinit var topicScrollState: Parcelable

  private var fabMenu = FabMenu(isMenuExpanded = false)
  private var isLoggedIn = false
  private var isKarmaAvailable = false

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

    subscribeViews()

    topicScrollState = topic_posts_list.layoutManager.onSaveInstanceState()
  }

  override fun onBackPressed(): Boolean {
    if (fabMenu.isMenuExpanded) {
      collapseMenu()
      return true
    }

    return false
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator() {
    topic_refresh_layout.isRefreshing = true
  }

  override fun hideLoadingIndicator() {
    topic_refresh_layout.isRefreshing = false
  }

  override fun appendPostItem(post: YapEntity) {
    topicAdapter.addPostItem(post)
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
    presenter.setAppbarTitle(title)
    presenter.setNavDrawerItem(NavigationSection.FORUMS)
  }

  override fun initiateTopicLoading() {
    presenter.loadTopic(forumId, topicId, startingPost)
  }

  override fun showUserProfile(userId: Int) {
    presenter.navigateToUserProfile(userId)
  }

  override fun shareTopic(title: String, topicPage: Int) {
    context?.share("http://www.yaplakal.com/forum$forumId/st/$topicPage/topic$topicId.html", title)
  }

  override fun showPostKarmaMenu(postId: Int) {
    val plusItem = context?.stringRes(R.string.action_post_karma_plus)
    val minusItem = context?.stringRes(R.string.action_post_karma_minus)

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
    val plusItem = context?.stringRes(R.string.action_topic_karma_plus)
    val minusItem = context?.stringRes(R.string.action_topic_karma_minus)

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

  override fun saveScrollPosition() {
    topicScrollState = topic_posts_list.layoutManager.onSaveInstanceState()
  }

  override fun restoreScrollPosition() {
    topic_posts_list.layoutManager.onRestoreInstanceState(topicScrollState)
  }

  override fun scrollToViewTop() {
    topic_posts_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showCantLoadPageMessage(page: Int) {
    context?.stringRes(R.string.navigation_page_not_available)?.let { template ->
      toastWarning(String.format(Locale.getDefault(), template, page))
    }
  }

  override fun showBookmarkAddedMessage() {
    context?.stringRes(R.string.msg_bookmark_topic_added)?.let { message ->
      toastSuccess(message)
    }
  }

  override fun showPostKarmaChangedMessage(isTopic: Boolean) {
    val message = if (isTopic) context?.stringRes(R.string.msg_karma_changed_topic)
    else context?.stringRes(R.string.msg_karma_changed_post)

    message?.let { text ->
      toastSuccess(text)
    }
  }

  override fun showPostAlreadyRatedMessage(isTopic: Boolean) {
    val message = if (isTopic) context?.stringRes(R.string.msg_karma_already_rated_topic)
    else context?.stringRes(R.string.msg_karma_already_rated_post)

    message?.let { text ->
      toastInfo(text)
    }
  }

  override fun showUnknownErrorMessage() {
    context?.stringRes(R.string.msg_unknown_error)?.let { message ->
      toastError(message)
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

  override fun loadThumbnail(videoUrl: String, imageView: ImageView) {
    presenter
      .requestThumbnail(videoUrl)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe({ url ->
        if (url.isNotEmpty()) {
          imageView.loadThumbnailFromUrl(url)
        } else {
          context?.let { imageView.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_othervideo)) }
        }
      }, { throwable ->
        Timber.e("Can't load image: ${throwable.message}")
      })
  }

  override fun onPostItemClicked(postId: Int, isKarmaAvailable: Boolean) {
    if (isKarmaAvailable) {
      presenter.showPostKarmaMenuIfAvailable(postId)
    }
  }

  override fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean) {
    when {
      isVideo && url.contains("youtube") -> {
        val videoId = url.extractYoutubeVideoId()
        context?.browse("http://www.youtube.com/watch?v=$videoId")
      }
      isVideo && !url.contains("youtube") -> {
        presenter.navigateToChosenVideo(html)
      }
      url.endsWith(GIF_EXT) -> {
        presenter.navigateToChosenGif(url)
      }
      else -> {
        presenter.navigateToChosenImage(url)
      }
    }
  }

  override fun onGoToFirstPageClick() {
    presenter.goToFirstPage()
  }

  override fun onGoToLastPageClick() {
    presenter.goToLastPage()
  }

  override fun onGoToPreviousPageClick() {
    presenter.goToPreviousPage()
  }

  override fun onGoToNextPageClick() {
    presenter.goToNextPage()
  }

  override fun onGoToSelectedPageClick() {
    view?.context?.let { ctx ->
      MaterialDialog.Builder(ctx)
        .title(R.string.navigation_go_to_page_title)
        .inputType(InputType.TYPE_CLASS_NUMBER)
        .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
          presenter.goToChosenPage(input.toString().toInt())
        })
        .show()
    }
  }

  override fun onUserAvatarClicked(userId: Int) {
    presenter.onUserProfileClicked(userId)
  }

  override fun onReplyButtonClicked(authorNickname: String, postDate: String, postId: Int) {
    presenter.onReplyButtonClicked(forumId, topicId, authorNickname, postDate, postId)
  }

  override fun onEditButtonClicked(postId: Int) {
    presenter.onEditButtonClicked(postId)
  }

  private fun subscribeViews() {

    RxSwipeRefreshLayout
      .refreshes(topic_refresh_layout)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { presenter.refreshCurrentPage() }

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
  }
}
