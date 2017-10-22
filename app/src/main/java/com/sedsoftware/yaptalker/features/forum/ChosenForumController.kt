package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseController
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.Topic
import com.sedsoftware.yaptalker.features.topic.ChosenTopicController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_chosen_forum.view.*

class ChosenForumController(val bundle: Bundle) : BaseController(bundle), ChosenForumView {

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

    forumPresenter.checkSavedState(currentForumId, savedViewState)
  }

  override fun subscribeViews(parent: View) {

    parent.forum_refresh_layout?.let {
      RxSwipeRefreshLayout
          .refreshes(parent.forum_refresh_layout)
          .autoDisposeWith(scopeProvider)
          .subscribe { forumPresenter.loadForum(currentForumId) }
    }
  }

  override fun onSaveViewState(view: View, outState: Bundle) {
    super.onSaveViewState(view, outState)
    val topics = forumAdapter.getTopics()
    if (topics.isNotEmpty()) {
      forumPresenter.saveCurrentState(outState, topics)
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


  override fun scrollToViewTop() {
    view?.forum_topics_list?.layoutManager?.scrollToPosition(0)
  }
}
