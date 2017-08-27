package com.sedsoftware.yaptalker.features.topic

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setAppColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.model.TopicPost
import com.sedsoftware.yaptalker.features.base.BaseController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_chosen_topic.view.*
import kotlinx.android.synthetic.main.include_navigation_panel.view.*
import java.util.Locale

class ChosenTopicController(val bundle: Bundle) : BaseController(bundle), ChosenTopicView {

  companion object {
    private const val POSTS_LIST_KEY = "POSTS_LIST_KEY"
    const val FORUM_ID_KEY = "FORUM_ID_KEY"
    const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
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

  override val controllerLayoutId: Int
    get() = R.layout.controller_chosen_topic

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    topicAdapter = ChosenTopicAdapter()
    topicAdapter.setHasStableIds(true)

    with(view.topic_refresh_layout) {
      setAppColorScheme()
    }

    with(view.topic_posts_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = topicAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    topicPresenter.checkSavedState(currentForumId, currentTopicId, savedViewState, POSTS_LIST_KEY)
  }

  override fun subscribeViews(parent: View) {
    parent.topic_refresh_layout?.let {
      RxSwipeRefreshLayout
          .refreshes(parent.topic_refresh_layout)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.loadTopic(currentForumId, currentTopicId) }
    }

    val buttonPrevious = parent.navigation_go_previous

    buttonPrevious?.let {
      RxView
          .clicks(buttonPrevious)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.goToPreviousPage() }
    }

    val buttonNext = parent.navigation_go_next

    buttonNext?.let {
      RxView
          .clicks(buttonNext)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.goToNextPage() }
    }

    val pagesLabel = parent.navigation_pages_label

    pagesLabel?.let {
      RxView
          .clicks(pagesLabel)
          .autoDisposeWith(scopeProvider)
          .subscribe { topicPresenter.goToChosenPage() }
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
}