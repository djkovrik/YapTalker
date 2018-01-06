package com.sedsoftware.yaptalker.presentation.features.topic

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
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
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.loadThumbnailFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastInfo
import com.sedsoftware.yaptalker.presentation.extensions.toastSuccess
import com.sedsoftware.yaptalker.presentation.extensions.toastWarning
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicAdapter
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicElementsClickListener
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicThumbnailLoader
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

@Suppress("TooManyFunctions")
class ChosenTopicFragment :
    BaseFragment(), ChosenTopicView, ChosenTopicThumbnailLoader, ChosenTopicElementsClickListener {

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

  override val layoutId: Int
    get() = R.layout.fragment_chosen_topic

  @Inject
  @InjectPresenter
  lateinit var presenter: ChosenTopicPresenter

  @ProvidePresenter
  fun provideTopicPresenter() = presenter

  @Inject
  lateinit var settings: SettingsManager

  private val forumId: Int by lazy {
    arguments?.getInt(FORUM_ID_KEY) ?: 0
  }

  private val topicId: Int by lazy {
    arguments?.getInt(TOPIC_ID_KEY) ?: 0
  }

  private val startingPost: Int by lazy {
    arguments?.getInt(STARTING_POST_KEY) ?: 0
  }

  private lateinit var menuShowAnimator: AnimatorSet
  private lateinit var menuHideAnimator: AnimatorSet
  private lateinit var topicAdapter: ChosenTopicAdapter

  private var isLoggedIn = false
  private var isKarmaAvailable = false
  private var fabMenu = FabMenu(isMenuExpanded = false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    menuShowAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_vertical_show) as AnimatorSet
    menuHideAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_vertical_hide) as AnimatorSet

    topicAdapter = ChosenTopicAdapter(this, this, settings)
    topicAdapter.setHasStableIds(true)

    with(topic_posts_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = topicAdapter
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      setHasFixedSize(true)
    }

    topic_refresh_layout.setIndicatorColorScheme()

    subscribeViews()
  }

  override fun onBackPressed(): Boolean {
    if (fabMenu.isMenuExpanded) {
      fabMenu.hideItems()
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

  override fun displayPostKarmaMenu(postId: Int, postPosition: Int) {
    val plusItem = context?.stringRes(R.string.action_post_karma_plus)
    val minusItem = context?.stringRes(R.string.action_post_karma_minus)

    val itemsArray = arrayListOf(plusItem, minusItem)

    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.title_post_context_menu)
          .items(itemsArray)
          .itemsCallback { _, _, _, text ->
            if (text == plusItem) presenter.changePostKarma(postId, postPosition, shouldIncrease = true)
            if (text == minusItem) presenter.changePostKarma(postId, postPosition, shouldIncrease = false)
          }
          .show()
    }
  }

  override fun displayTopicKarmaMenu() {
    val plusItem = context?.stringRes(R.string.action_topic_karma_plus)
    val minusItem = context?.stringRes(R.string.action_topic_karma_minus)

    val itemsArray = arrayListOf(plusItem, minusItem)

    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.title_topic_karma_menu)
          .items(itemsArray)
          .itemsCallback { _, _, _, text ->
            if (text == plusItem) {
              fabMenu.hideItems()
              presenter.changeTopicKarma(shouldIncrease = true)
            }
            if (text == minusItem) {
              fabMenu.hideItems()
              presenter.changeTopicKarma(shouldIncrease = false)
            }
          }
          .show()
    }
  }

  override fun scrollToViewTop() {
    topic_posts_list?.layoutManager?.scrollToPosition(0)
    Timber.d("Scroll to page top")
  }

  override fun scrollToPost(position: Int) {
    topic_posts_list?.layoutManager?.scrollToPosition(position)
    Timber.d("Scroll to post $position")
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

  override fun showPostKarmaChangedMessage() {
    context?.stringRes(R.string.msg_karma_changed)?.let { message ->
      toastSuccess(message)
    }
  }

  override fun showPostAlreadyRatedMessage() {
    context?.stringRes(R.string.msg_karma_already_rated)?.let { message ->
      toastInfo(message)
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

  override fun onPostItemClicked(postId: Int, postPosition: Int, isKarmaAvailable: Boolean) {
    if (isKarmaAvailable) {
      presenter.showPostKarmaMenuIfAvailable(postId, postPosition)
    }
  }

  override fun onUserAvatarClick(userId: Int) {
    presenter.onUserProfileClicked(userId)
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

  private fun subscribeViews() {

    RxSwipeRefreshLayout
        .refreshes(topic_refresh_layout)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe { presenter.refreshCurrentPage() }

    RxView
        .clicks(fab_menu)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe { initiateFabMenuDisplaying() }

    RxView
        .clicks(fab_refresh)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe {
          fabMenu.hideItems()
          presenter.refreshCurrentPage()
        }

    RxView
        .clicks(fab_bookmark)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe {
          fabMenu.hideItems()
          presenter.addCurrentTopicToBookmarks()
        }

    RxView
        .clicks(fab_share)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe {
          fabMenu.hideItems()
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
          fabMenu.hideItems()
          presenter.navigateToMessagePostingScreen()
        }

    RxView
        .clicks(fab_overlay)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe { fabMenu.hideItems() }
  }

  private fun initiateFabMenuDisplaying() {
    if (fabMenu.isMenuExpanded) {
      fabMenu.hideItems()

    } else {
      refreshFabMenuState()
      fabMenu.showItems()
    }
  }

  private fun refreshFabMenuState() {

    fabMenu.clear()

    fabMenu.add(FabOverlay(context, fab_overlay))
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
