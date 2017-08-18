package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.setAppColorScheme
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.Topic
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_chosen_forum.view.*

class ChosenForumController(val bundle: Bundle) : BaseController(bundle), ChosenForumView {

  companion object {
    const val FORUM_ID_KEY = "FORUM_ID_KEY"
    private const val TOPICS_LIST_KEY = "TOPICS_LIST"
  }

  val forumId: Int by lazy {
    bundle.getInt(FORUM_ID_KEY)
  }

  @InjectPresenter
  lateinit var forumPresenter: ChosenForumPresenter

  lateinit var forumAdapter: ChosenForumAdapter

  override val controllerLayoutId: Int
    get() = R.layout.controller_chosen_forum

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    forumAdapter = ChosenForumAdapter()
    forumAdapter.setHasStableIds(true)

    with(view.forum_refresh_layout) {
      setOnRefreshListener { forumPresenter.loadForum(forumId) }
      setAppColorScheme()
    }

    with(view.forum_topics_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumAdapter

      setHasFixedSize(true)
    }

    if (savedViewState != null && savedViewState.containsKey(TOPICS_LIST_KEY)) {
      val topics = savedViewState.getParcelableArrayList<Topic>(TOPICS_LIST_KEY)
      forumAdapter.setTopics(topics)
    } else {
      forumPresenter.loadForum(forumId)
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

  override fun showRefreshing() {
    view?.forum_refresh_layout?.isRefreshing = true
  }

  override fun hideRefreshing() {
    view?.forum_refresh_layout?.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun refreshTopics(topics: List<Topic>) {
    forumAdapter.setTopics(topics)
  }
}