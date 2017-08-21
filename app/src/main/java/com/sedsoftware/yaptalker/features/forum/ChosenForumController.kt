package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.setAppColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.Topic
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_chosen_forum.view.*
import kotlinx.android.synthetic.main.include_navigation_panel.view.*
import java.util.Locale

class ChosenForumController(val bundle: Bundle) : BaseController(bundle), ChosenForumView {

  companion object {
    const val TOPICS_LIST_KEY = "TOPICS_LIST"
    const val FORUM_ID_KEY = "FORUM_ID_KEY"
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

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    forumPresenter.checkSavedState(forumId, savedViewState)
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

  // TODO() Add animation here because now it looks awful
  override fun setIfNavigationPanelVisible(isVisible: Boolean) {
    when (isVisible) {
      true -> view?.navigation_panel?.visibility = View.VISIBLE
      else -> view?.navigation_panel?.visibility = View.GONE
    }
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
}
