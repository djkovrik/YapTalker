package com.sedsoftware.yaptalker.features.forumslist

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_forums_list.view.*

class ForumsController : BaseController(), ForumsView {

  companion object {
    private const val FORUMS_LIST_KEY = "FORUMS_LIST_KEY"
  }

  @InjectPresenter
  lateinit var forumsPresenter: ForumsPresenter

  lateinit var forumsAdapter: ForumsAdapter

  override val controllerLayoutId: Int
    get() = R.layout.controller_forums_list

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    forumsAdapter = ForumsAdapter()
    forumsAdapter.setHasStableIds(true)

    with(view.forums_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumsAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      setHasFixedSize(true)
    }

    if (savedViewState != null && savedViewState.containsKey(FORUMS_LIST_KEY)) {
      val forums = savedViewState.getParcelableArrayList<ForumItem>(FORUMS_LIST_KEY)
      forumsAdapter.addForumsList(forums)
    } else {
      forumsPresenter.loadForumsList()
    }
  }

  override fun onSaveViewState(view: View, outState: Bundle) {
    super.onSaveViewState(view, outState)
    val forums = forumsAdapter.getForumsList()
    outState.putParcelableArrayList(FORUMS_LIST_KEY, forums)
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    view.forums_list.adapter = null
  }

  override fun showForums(forums: List<ForumItem>) {
    forumsAdapter.addForumsList(forums)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showProgressBar() {
    view?.forums_list?.visibility = View.GONE
    view?.forums_list_loading?.visibility = View.VISIBLE
  }

  override fun hideProgressBar() {
    view?.forums_list_loading?.visibility = View.GONE
    view?.forums_list?.visibility = View.VISIBLE
  }
}
