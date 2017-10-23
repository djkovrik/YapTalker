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
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseController
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.features.forum.adapter.ChosenForumAdapter
import com.sedsoftware.yaptalker.features.forum.adapter.NavigationItemClickListener
import com.sedsoftware.yaptalker.features.forum.adapter.TopicItemClickListener
import com.sedsoftware.yaptalker.features.topic.ChosenTopicController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_chosen_forum.view.*
import java.util.Locale

class ChosenForumController(val bundle: Bundle) :
    BaseController(bundle), ChosenForumView, TopicItemClickListener, NavigationItemClickListener {

  companion object {
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

    forumAdapter = ChosenForumAdapter(this, this)

    forumAdapter.setHasStableIds(true)

    view.forum_refresh_layout.setIndicatorColorScheme()

    with(view.forum_topics_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    forumPresenter.checkSavedState(currentForumId, savedViewState)
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    view.forum_topics_list.adapter = null
  }

  override fun onSaveViewState(view: View, outState: Bundle) {
    super.onSaveViewState(view, outState)
    val panel = forumAdapter.getNavigationPanel()
    val topics = forumAdapter.getTopics()

    if (panel.isNotEmpty() && topics.isNotEmpty()) {
      forumPresenter.saveCurrentState(outState, currentForumId, panel.first(), topics)
    }
  }

  override fun subscribeViews(parent: View) {
    parent.forum_refresh_layout?.let {
      RxSwipeRefreshLayout
          .refreshes(parent.forum_refresh_layout)
          .autoDisposeWith(scopeProvider)
          .subscribe { forumPresenter.loadForum(currentForumId) }
    }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator(shouldShow: Boolean) {
    view?.forum_refresh_layout?.isRefreshing = shouldShow
  }

  override fun displayForumPage(page: ForumPage) {
    forumAdapter.refreshForumPage(page)
  }

  override fun scrollToViewTop() {
    view?.forum_topics_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showCantLoadPageMessage(page: Int) {
    view?.context?.stringRes(R.string.navigation_page_not_available)?.let { template ->
      toastWarning(String.format(Locale.getDefault(), template, page))
    }
  }

  override fun onTopicClick(topicId: Int) {
    val bundle = Bundle()
    bundle.putInt(ChosenTopicController.FORUM_ID_KEY, currentForumId)
    bundle.putInt(ChosenTopicController.TOPIC_ID_KEY, topicId)
    router.pushController(
        RouterTransaction.with(ChosenTopicController(bundle))
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler()))
  }

  override fun onGoToFirstPageClick() {
    forumPresenter.goToFirstPage()
  }

  override fun onGoToLastPageClick() {
    forumPresenter.goToLastPage()
  }

  override fun onGoToPreviousPageClick() {
    forumPresenter.goToPreviousPage()
  }

  override fun onGoToNextPageClick() {
    forumPresenter.goToNextPage()
  }

  override fun onGoToSelectedPageClick() {
    view?.context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.navigation_go_to_page_title)
          .inputType(InputType.TYPE_CLASS_NUMBER)
          .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
            forumPresenter.goToChosenPage(input.toString().toInt())
          })
          .show()
    }
  }
}
