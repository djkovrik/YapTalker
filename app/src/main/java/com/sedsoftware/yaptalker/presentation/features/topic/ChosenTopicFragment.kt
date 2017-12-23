package com.sedsoftware.yaptalker.presentation.features.topic

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastSuccess
import com.sedsoftware.yaptalker.presentation.extensions.toastWarning
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicAdapter
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicItemClickListener
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicThumbnailLoader
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.TopicNavigationClickListener
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.UserProfileClickListener
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chosen_topic.*
import org.jetbrains.anko.share
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class ChosenTopicFragment : BaseFragment(), ChosenTopicView, ChosenTopicItemClickListener, ChosenTopicThumbnailLoader,
    TopicNavigationClickListener, UserProfileClickListener {

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
  }

  override val layoutId: Int
    get() = R.layout.fragment_chosen_topic

  @Inject
  lateinit var settings: SettingsManager

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

  private lateinit var topicAdapter: ChosenTopicAdapter

  private var isLoggedIn = false
  private var isKarmaAvailable = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setHasOptionsMenu(true)

    topicAdapter = ChosenTopicAdapter(this, this, this, this, settings)
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

  override fun onPrepareOptionsMenu(menu: Menu?) {
    super.onPrepareOptionsMenu(menu)

    menu?.findItem(R.id.action_bookmark)?.isVisible = isLoggedIn
    menu?.findItem(R.id.action_new_message)?.isVisible = isLoggedIn
    menu?.findItem(R.id.action_topic_karma_plus)?.isVisible = isKarmaAvailable
    menu?.findItem(R.id.action_topic_karma_minus)?.isVisible = isKarmaAvailable
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_chosen_topic, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
      when (item.itemId) {
        R.id.action_share -> {
          presenter.shareCurrentTopic()
          true
        }
        R.id.action_new_message -> {
          presenter.showNewMessagePostingScreen()
          true
        }
        R.id.action_topic_karma_plus -> {
          presenter.changeTopicKarma(shouldIncrease = true)
          true
        }
        R.id.action_topic_karma_minus -> {
          presenter.changeTopicKarma(shouldIncrease = false)
          true
        }
        R.id.action_bookmark -> {
          presenter.addCurrentTopicToBookmarks()
          true
        }
        else -> super.onOptionsItemSelected(item)
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

  override fun displayPostContextMenu(postId: Int) {
    val plusItem = context?.stringRes(R.string.action_post_karma_plus)
    val minusItem = context?.stringRes(R.string.action_post_karma_minus)

    val itemsArray = arrayListOf(plusItem, minusItem)

    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.title_post_context_menu)
          .items(itemsArray)
          .itemsCallback { _, _, _, text ->
            if (text == plusItem) presenter.changePostKarma(postId, shouldIncrease = true)
            if (text == minusItem) presenter.changePostKarma(postId, shouldIncrease = false)
          }
          .show()
    }
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

  override fun onPostItemClicked(postId: Int, isKarmaAvailable: Boolean) {
    if (isKarmaAvailable) {
      presenter.showPostContextMenuIfAvailable(postId)
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
        .autoDisposable(event(FragmentLifecycle.STOP))
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
        .autoDisposable(event(FragmentLifecycle.STOP))
        .subscribe { presenter.refreshCurrentPage() }
  }
}
