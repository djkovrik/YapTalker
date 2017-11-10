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
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastSuccess
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.parsing.TopicPage
import com.sedsoftware.yaptalker.features.topic.adapter.ChosenTopicAdapter
import com.sedsoftware.yaptalker.features.topic.adapter.ChosenTopicItemClickListener
import com.sedsoftware.yaptalker.features.topic.adapter.TopicNavigationClickListener
import com.sedsoftware.yaptalker.features.topic.adapter.UserProfileClickListener
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_chosen_topic.*
import org.jetbrains.anko.share
import java.util.Locale

@Suppress("TooManyFunctions")
class ChosenTopicFragment : BaseFragment(), ChosenTopicView, UserProfileClickListener, TopicNavigationClickListener,
    ChosenTopicItemClickListener {

  companion object {
    private const val FORUM_ID_KEY = "FORUM_ID_KEY"
    private const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
    private const val STARTING_POST_KEY = "STARTING_POST_KEY"

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

    topicAdapter = ChosenTopicAdapter(this, this, this)
    topicAdapter.setHasStableIds(true)

    topic_refresh_layout.setIndicatorColorScheme()

    with(topic_posts_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = topicAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_chosen_topic, menu)
  }

  override fun onPrepareOptionsMenu(menu: Menu?) {
    super.onPrepareOptionsMenu(menu)

    menu?.findItem(R.id.action_bookmark)?.isVisible = isLoggedIn
    menu?.findItem(R.id.action_new_message)?.isVisible = isLoggedIn
    menu?.findItem(R.id.action_topic_karma_plus)?.isVisible = isKarmaAvailable
    menu?.findItem(R.id.action_topic_karma_minus)?.isVisible = isKarmaAvailable
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_share -> {
        topicPresenter.onShareItemClicked()
        true
      }
      R.id.action_new_message -> {
        topicPresenter.onNewMessageItemClicked()
        true
      }
      R.id.action_topic_karma_plus -> {
        topicPresenter.onChangeTopicKarmaItemClicked(increaseKarma = true)
        true
      }
      R.id.action_topic_karma_minus -> {
        topicPresenter.onChangeTopicKarmaItemClicked(increaseKarma = false)
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
  }

  override fun updateAppbarTitle(title: String) {
    topicPresenter.setAppbarTitle(title)
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

  override fun displayTopicPage(page: TopicPage) {
    topicAdapter.refreshTopicPage(page)
  }

  override fun setLoggedInState(isLoggedIn: Boolean) {
    this.isLoggedIn = isLoggedIn
  }

  override fun setTopicKarmaState(isKarmaAvailable: Boolean) {
    this.isKarmaAvailable = isKarmaAvailable
  }

  override fun initiateTopicLoading() {
    topicPresenter.loadTopic(forumId, topicId, startingPost)
  }

  override fun showUserProfile(userId: Int) {
    topicPresenter.navigateToUserProfile(userId)
  }

  override fun shareTopic(title: String, topicPage: Int) {
    context?.share("http://www.yaplakal.com/forum$forumId/st/$topicPage/topic$topicId.html", title)
  }

  override fun displayPostContextMenu(postId: String) {
    val plusItem = context?.stringRes(R.string.action_post_karma_plus)
    val minusItem = context?.stringRes(R.string.action_post_karma_minus)

    val itemsArray = arrayListOf(plusItem, minusItem)

    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.title_post_context_menu)
          .items(itemsArray)
          .itemsCallback { _, _, _, text ->
            if (text == plusItem) topicPresenter.onChangePostKarmaItemClicked(postId, increaseKarma = true)
            if (text == minusItem) topicPresenter.onChangePostKarmaItemClicked(postId, increaseKarma = false)
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

  override fun onUserAvatarClick(userId: Int) {
    topicPresenter.onUserProfileClicked(userId)
  }

  override fun onPostItemClicked(postId: String, isKarmaAvailable: Boolean) {
    if (isKarmaAvailable) {
      topicPresenter.checkIfPostContextMenuAvailable(postId)
    }
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
