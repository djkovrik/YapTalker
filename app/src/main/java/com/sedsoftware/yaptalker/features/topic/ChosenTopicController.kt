package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.hideBeyondScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.showFromScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.model.TopicPost
import com.sedsoftware.yaptalker.features.base.BaseController
import com.sedsoftware.yaptalker.features.posting.AddMessageActivity
import com.sedsoftware.yaptalker.features.userprofile.UserProfileController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_chosen_topic.view.*
import kotlinx.android.synthetic.main.include_navigation_panel.view.*
import org.jetbrains.anko.startActivity
import java.util.Locale


// TODO() Add share command
// TODO() Check if navigation can be replaced with fabs
class ChosenTopicController(val bundle: Bundle) : BaseController(bundle), ChosenTopicView {

  companion object {
    const val FORUM_ID_KEY = "FORUM_ID_KEY"
    const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
    const val TOPIC_TITLE_KEY = "TOPIC_TITLE_KEY"
    private const val POSTS_LIST_KEY = "POSTS_LIST_KEY"
    private const val INITIAL_FAB_OFFSET = 250f
  }

  private val currentForumId: Int by lazy {
    bundle.getInt(FORUM_ID_KEY)
  }

  private val currentTopicId: Int by lazy {
    bundle.getInt(TOPIC_ID_KEY)
  }

  @InjectPresenter
  lateinit var topicPresenter: ChosenTopicPresenter

  private lateinit var topicAdapter: ChosenTopicAdapter
  private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
  private var isNavigationShown = true
  private var isFabShown = true

  override val controllerLayoutId: Int
    get() = R.layout.controller_chosen_topic

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    topicAdapter = ChosenTopicAdapter { userId ->
      val bundle = Bundle()
      bundle.putInt(UserProfileController.USER_ID_KEY, userId)
      router.pushController(
          RouterTransaction.with(UserProfileController(bundle))
              .pushChangeHandler(FadeChangeHandler())
              .popChangeHandler(FadeChangeHandler()))
    }

    topicAdapter.setHasStableIds(true)

    view.topic_refresh_layout.setIndicatorColorScheme()

    with(view.topic_posts_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = topicAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    topicPresenter.checkSavedState(currentForumId, currentTopicId, savedViewState, POSTS_LIST_KEY)

    bottomSheetBehavior = BottomSheetBehavior.from(view.navigation_panel)
  }

  override fun subscribeViews(parent: View) {
    parent.topic_refresh_layout?.let {
      RxSwipeRefreshLayout
          .refreshes(parent.topic_refresh_layout)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.loadTopic(currentForumId, currentTopicId) }
    }

    parent.navigation_go_previous?.let {
      RxView
          .clicks(parent.navigation_go_previous)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.goToPreviousPage() }
    }

    parent.navigation_go_next?.let {
      RxView
          .clicks(parent.navigation_go_next)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.goToNextPage() }
    }

    parent.navigation_pages_label?.let {
      RxView
          .clicks(parent.navigation_pages_label)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.goToChosenPage() }
    }

    parent.topic_posts_list?.let {
      RxRecyclerView
          .scrollEvents(parent.topic_posts_list)
          .autoDisposeWith(scopeProvider)
          .subscribe { event ->
            topicPresenter.handleNavigationVisibility(isFabShown, isNavigationShown, event.dy())
          }
    }

    parent.new_post_fab?.let {
      RxView
          .clicks(parent.new_post_fab)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.onFabClicked(currentForumId, currentTopicId) }
    }
  }

  override fun onSaveViewState(view: View, outState: Bundle) {
    super.onSaveViewState(view, outState)
    val posts = topicAdapter.getPosts()
    if (posts.isNotEmpty()) {
      outState.putParcelableArrayList(POSTS_LIST_KEY, posts)
    }
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    view.topic_posts_list.adapter = null
  }

  override fun showRefreshing() {
    view?.topic_refresh_layout?.isRefreshing = true
  }

  override fun hideRefreshing() {
    view?.topic_refresh_layout?.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun refreshPosts(posts: List<TopicPost>) {
    topicAdapter.setPosts(posts)
  }

  override fun setNavigationPagesLabel(page: Int, totalPages: Int) {
    val template = view?.context?.stringRes(R.string.navigation_pages_template) ?: ""

    if (template.isNotEmpty()) {
      view?.navigation_pages_label?.text = String.format(Locale.US, template, page, totalPages)
    }
  }

  override fun setIfNavigationBackEnabled(isEnabled: Boolean) {
    view?.navigation_go_previous?.isEnabled = isEnabled
  }

  override fun setIfNavigationForwardEnabled(isEnabled: Boolean) {
    view?.navigation_go_next?.isEnabled = isEnabled
  }

  override fun showGoToPageDialog(maxPages: Int) {

    view?.context?.let {
      MaterialDialog.Builder(it)
          .title(R.string.navigation_go_to_page_title)
          .inputType(InputType.TYPE_CLASS_NUMBER)
          .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
            topicPresenter.loadChosenTopicPage(input.toString().toInt())
          })
          .show()
    }
  }

  override fun showCantLoadPageMessage(page: Int) {
    val messageTemplate = view?.context?.stringRes(R.string.navigation_page_not_available)

    messageTemplate?.let {
      toastWarning(String.format(Locale.US, it, page))
    }
  }

  override fun scrollToViewTop() {
    view?.topic_posts_list?.layoutManager?.scrollToPosition(0)
  }

  override fun setAppbarTitle(title: String) {
    topicPresenter.setAppbarTitle(title)
  }

  override fun hideNavigationPanel() {
    isNavigationShown = false
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
  }

  override fun showNavigationPanel() {
    isNavigationShown = true
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
  }

  override fun hideFab() {
    if (!isFabShown) {
      return
    }

    view?.new_post_fab?.let { fab ->
      val offset = fab.height + fab.paddingTop + fab.paddingBottom
      fab.hideBeyondScreenEdge(offset.toFloat())
      isFabShown = false
    }
  }

  override fun hideFabWithoutAnimation() {
    view?.new_post_fab?.translationY = INITIAL_FAB_OFFSET
    isFabShown = false
  }

  override fun showFab() {
    if (isFabShown) {
      return
    }

    view?.new_post_fab?.let { fab ->
      fab.showFromScreenEdge()
      isFabShown = true
    }
  }

  override fun showAddMessageActivity(title: String, forumId: Int, topicId: Int) {
    view?.context?.startActivity<AddMessageActivity>(
        TOPIC_TITLE_KEY to title,
        FORUM_ID_KEY to forumId,
        TOPIC_ID_KEY to topicId)
  }
}
