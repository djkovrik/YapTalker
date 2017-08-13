package com.sedsoftware.yaptalker.features.forumslist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_forums_list.view.*

class ForumsController : BaseController(), ForumsView {

  @InjectPresenter
  lateinit var forumsPresenter: ForumsPresenter

  lateinit var forumsAdapter: ForumsAdapter

  override val controllerLayoutId: Int
    get() = R.layout.controller_forums_list

  // TODO() Check why onViewBound called twice
  override fun onViewBound(view: View, savedViewState: Bundle?) {

    forumsAdapter = ForumsAdapter()
    forumsAdapter.setHasStableIds(true)

    with(view.forums_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumsAdapter
      setHasFixedSize(true)
    }

    forumsPresenter.loadForumsList()
  }

  override fun onDetach(view: View) {
    super.onDetach(view)
    view.forums_list.adapter = null
  }

  override fun showForums(forums: List<ForumItem>) {
    forumsAdapter.addForumsList(forums)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }
}