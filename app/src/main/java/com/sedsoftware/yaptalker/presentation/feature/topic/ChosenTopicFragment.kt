package com.sedsoftware.yaptalker.presentation.feature.topic

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
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.moveWithAnimationAxisY
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.feature.topic.adapter.ChosenTopicAdapter
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabMenu
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabMenuItemPrimary
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabMenuItemSecondary
import com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu.FabOverlay
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.thumbnail.ThumbnailsLoader
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chosen_topic.*
import kotlinx.android.synthetic.main.include_topic_fab_menu.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.share
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@Suppress("LargeClass", "TooManyFunctions")
@LayoutResource(value = R.layout.fragment_chosen_topic)
class ChosenTopicFragment : BaseFragment(), ChosenTopicView, ThumbnailsLoader {

  companion object {
    fun getNewInstance(triple: Triple<Int, Int, Int>): ChosenTopicFragment =
      ChosenTopicFragment().apply {
        arguments = bundleOf(
          FORUM_ID_KEY to triple.first,
          TOPIC_ID_KEY to triple.second,
          STARTING_POST_KEY to triple.third
        )
      }

    private const val FORUM_ID_KEY = "FORUM_ID_KEY"
    private const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
    private const val STARTING_POST_KEY = "STARTING_POST_KEY"
  }

  @Inject
  lateinit var settings: Settings

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
    presenter.loadTopic(forumId, topicId, startingPost)
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

  override fun saveScrollPosition() {
    topicScrollState = topic_posts_list.layoutManager.onSaveInstanceState()
  }

  override fun restoreScrollPosition() {
    topic_posts_list.layoutManager.onRestoreInstanceState(topicScrollState)
  }

  override fun scrollToViewTop() {
    topic_posts_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showPageSelectionDialog() {
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

  override fun browseExternalResource(url: String) {
    context?.browse(url.validateUrl())
  }

  override fun loadThumbnail(videoUrl: String, imageView: ImageView) {
    presenter
      .requestThumbnail(videoUrl)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe({ url ->
        if (url.isNotEmpty()) {
          imageView.loadFromUrl(url)
        } else {
          context?.let { imageView.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_othervideo)) }
        }
      }, { throwable ->
        Timber.e("Can't load image: ${throwable.message}")
      })
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
      .clicks(fab_gallery)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe {
        collapseMenu()
        presenter.openTopicGallery()
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
  }
}
