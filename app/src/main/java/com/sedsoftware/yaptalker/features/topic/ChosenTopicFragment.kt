package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.extensions.hideBeyondScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.showFromScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastSuccess
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.parsing.TopicPage
import com.sedsoftware.yaptalker.features.topic.adapter.ChosenTopicAdapter
import com.sedsoftware.yaptalker.features.topic.adapter.TopicNavigationClickListener
import com.sedsoftware.yaptalker.features.topic.adapter.UserProfileClickListener
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_chosen_topic.*
import org.jetbrains.anko.share
import java.util.Locale

class ChosenTopicFragment : BaseFragment(), ChosenTopicView, UserProfileClickListener, TopicNavigationClickListener {

  companion object {
    const val MESSAGE_TEXT_REQUEST = 321
    private const val FORUM_ID_KEY = "FORUM_ID_KEY"
    private const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
    private const val STARTING_POST_KEY = "STARTING_POST_KEY"
    private const val INITIAL_FAB_OFFSET = 250f

    fun getNewInstance(triple: Triple<Int, Int, Int>): ChosenTopicFragment {
      val fragment = ChosenTopicFragment()
      val args = Bundle()
      args.putInt(FORUM_ID_KEY, triple.first)
      args.putInt(TOPIC_ID_KEY, triple.second)
      args.putInt(STARTING_POST_KEY, triple.third)
      fragment.arguments = args
      return fragment
    }
  }

  @InjectPresenter
  lateinit var topicPresenter: ChosenTopicPresenter

  override val layoutId: Int
    get() = R.layout.fragment_chosen_topic

  private val forumId: Int by lazy {
    arguments.getInt(FORUM_ID_KEY)
  }

  private val topicId: Int by lazy {
    arguments.getInt(TOPIC_ID_KEY)
  }

  private val startingPost: Int by lazy {
    arguments.getInt(STARTING_POST_KEY)
  }

  private lateinit var topicAdapter: ChosenTopicAdapter
  private lateinit var currentMenu: Menu
  private var isFabShown = true

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setHasOptionsMenu(true)

    topicAdapter = ChosenTopicAdapter(this, this)
    topicAdapter.setHasStableIds(true)

    topic_refresh_layout.setIndicatorColorScheme()

    with(topic_posts_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = topicAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    topicPresenter.checkSavedState(forumId, topicId, startingPost, savedInstanceState)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    val panel = topicAdapter.getNavigationPanel()
    val posts = topicAdapter.getPosts()

    if (panel.isNotEmpty() && posts.isNotEmpty()) {
      topicPresenter.saveCurrentState(outState, panel.first(), posts)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_chosen_topic, menu)
    currentMenu = menu
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_share -> {
        topicPresenter.onShareItemClicked()
        true
      }
      R.id.action_bookmark -> {
        topicPresenter.onBookmarkItemClicked()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun subscribeViews() {

    RxSwipeRefreshLayout
        .refreshes(topic_refresh_layout)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { topicPresenter.loadTopic(forumId, topicId) }

    RxRecyclerView
        .scrollEvents(topic_posts_list)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { event ->
          topicPresenter.handleFabVisibility(isFabShown, event.dy())
        }

    RxView
        .clicks(new_post_fab)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { topicPresenter.navigateToAddMessageView() }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator(shouldShow: Boolean) {
    topic_refresh_layout?.isRefreshing = shouldShow
  }

  override fun displayTopicPage(page: TopicPage) {
    topicAdapter.refreshTopicPage(page)
  }

  override fun scrollToViewTop() {
    topic_posts_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showFab(shouldShow: Boolean) {
    if (shouldShow == isFabShown) {
      return
    }

    if (shouldShow) {
      new_post_fab?.let { fab ->
        fab.showFromScreenEdge()
        isFabShown = true
      }
    } else {
      new_post_fab?.let { fab ->
        val offset = fab.height + fab.paddingTop + fab.paddingBottom
        fab.hideBeyondScreenEdge(offset.toFloat())
        isFabShown = false
      }
    }
  }

  override fun hideFabWithoutAnimation() {
    new_post_fab?.translationY = INITIAL_FAB_OFFSET
    isFabShown = false
  }

  override fun handleBookmarkButtonVisibility(shouldShow: Boolean) {
    currentMenu.findItem(R.id.action_bookmark).isVisible = shouldShow
  }

  override fun showCantLoadPageMessage(page: Int) {
    context?.stringRes(R.string.navigation_page_not_available)?.let { template ->
      toastWarning(String.format(Locale.getDefault(), template, page))
    }
  }

  override fun showUserProfile(userId: Int) {
    topicPresenter.navigateToUserProfile(userId)
  }

  override fun shareTopic(title: String, topicPage: Int) {
    context?.share("http://www.yaplakal.com/forum$forumId/st/$topicPage/topic$topicId.html", title)
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

  override fun onUserAvatarClick(userId: Int) {
    topicPresenter.loadProfileIfAvailable(userId)
  }

  override fun onGoToFirstPageClick() {
    topicPresenter.goToFirstPage()
  }

  override fun onGoToLastPageClick() {
    topicPresenter.goToLastPage()
  }

  override fun onGoToPreviousPageClick() {
    topicPresenter.goToPreviousPage()
  }

  override fun onGoToNextPageClick() {
    topicPresenter.goToNextPage()
  }

  override fun onGoToSelectedPageClick() {
    view?.context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.navigation_go_to_page_title)
          .inputType(InputType.TYPE_CLASS_NUMBER)
          .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
            topicPresenter.goToChosenPage(input.toString().toInt())
          })
          .show()
    }
  }
}
