package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseController
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.model.Topic
import com.sedsoftware.yaptalker.features.topic.ChosenTopicController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_chosen_forum.view.*
import kotlinx.android.synthetic.main.include_navigation_panel.view.*
import java.util.Locale

class ChosenForumController(val bundle: Bundle) : BaseController(bundle), ChosenForumView {

  companion object {
    private const val TOPICS_LIST_KEY = "TOPICS_LIST"
    const val FORUM_ID_KEY = "FORUM_ID_KEY"
  }

  private val currentForumId: Int by lazy {
    bundle.getInt(FORUM_ID_KEY)
  }

  @InjectPresenter
  lateinit var forumPresenter: ChosenForumPresenter

  private lateinit var forumAdapter: ChosenForumAdapter

  override val controllerLayoutId: Int
    get() = R.layout.controller_chosen_forum

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    forumAdapter = ChosenForumAdapter {
      val bundle = Bundle()
      bundle.putInt(ChosenTopicController.FORUM_ID_KEY, currentForumId)
      bundle.putInt(ChosenTopicController.TOPIC_ID_KEY, it)
      router.pushController(
          RouterTransaction.with(ChosenTopicController(bundle))
              .pushChangeHandler(FadeChangeHandler())
              .popChangeHandler(FadeChangeHandler()))
    }

    forumAdapter.setHasStableIds(true)

    view.forum_refresh_layout.setIndicatorColorScheme()

    with(view.forum_topics_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    forumPresenter.checkSavedState(currentForumId, savedViewState, TOPICS_LIST_KEY)
  }

  override fun subscribeViews(parent: View) {

    parent.forum_refresh_layout?.let {
      RxSwipeRefreshLayout
          .refreshes(parent.forum_refresh_layout)
          .autoDisposeWith(scopeProvider)
          .subscribe { forumPresenter.loadForum(currentForumId) }
    }

    parent.navigation_go_previous?.let {
      RxView
          .clicks(parent.navigation_go_previous)
          .autoDisposeWith(scopeProvider)
          .subscribe { forumPresenter.goToPreviousPage() }
    }

    parent.navigation_go_next?.let {
      RxView
          .clicks(parent.navigation_go_next)
          .autoDisposeWith(scopeProvider)
          .subscribe { forumPresenter.goToNextPage() }
    }

    parent.navigation_pages_label?.let {
      RxView
          .clicks(parent.navigation_pages_label)
          .autoDisposeWith(scopeProvider)
          .subscribe { forumPresenter.goToChosenPage() }
    }
  }

  override fun onSaveViewState(view: View, outState: Bundle) {
    super.onSaveViewState(view, outState)
    val topics = forumAdapter.getTopics()
    if (topics.isNotEmpty()) {
      outState.putParcelableArrayList(TOPICS_LIST_KEY, topics)
    }
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    view.forum_topics_list.adapter = null
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator(shouldShow: Boolean) {
    view?.forum_refresh_layout?.isRefreshing = shouldShow
  }

  override fun refreshTopics(topics: List<Topic>) {
    forumAdapter.setTopics(topics)
  }

  override fun setNavigationPagesLabel(page: Int, totalPages: Int) {
    val template = view?.context?.stringRes(R.string.navigation_pages_template) ?: ""

    if (template.isNotEmpty()) {
      view?.navigation_pages_label?.text = String.format(Locale.getDefault(), template, page, totalPages)
    }
  }

  override fun setIfNavigationBackEnabled(isEnabled: Boolean) {
    view?.navigation_go_previous?.isEnabled = isEnabled
  }

  override fun setIfNavigationForwardEnabled(isEnabled: Boolean) {
    view?.navigation_go_next?.isEnabled = isEnabled
  }

  override fun scrollToViewTop() {
    view?.forum_topics_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showGoToPageDialog(maxPages: Int) {
    view?.context?.let {
      MaterialDialog.Builder(it)
          .title(R.string.navigation_go_to_page_title)
          .inputType(InputType.TYPE_CLASS_NUMBER)
          .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
            forumPresenter.loadChosenForumPage(input.toString().toInt())
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
