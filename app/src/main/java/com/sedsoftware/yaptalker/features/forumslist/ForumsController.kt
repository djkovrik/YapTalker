package com.sedsoftware.yaptalker.features.forumslist

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.setAppColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.base.BaseController
import com.sedsoftware.yaptalker.features.forum.ChosenForumController
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.controller_forums_list.view.*
import kotlinx.android.synthetic.main.controller_news.view.*

class ForumsController : BaseController(), ForumsView {

  @InjectPresenter
  lateinit var forumsPresenter: ForumsPresenter

  private lateinit var forumsAdapter: ForumsAdapter

  override val controllerLayoutId: Int
    get() = R.layout.controller_forums_list

  override fun onViewBound(view: View, savedViewState: Bundle?) {

    forumsAdapter = ForumsAdapter {
      // Load chosen forum
      val bundle = Bundle()
      bundle.putInt(ChosenForumController.FORUM_ID_KEY, it)
      router.pushController(
          RouterTransaction.with(ChosenForumController(bundle))
              .pushChangeHandler(FadeChangeHandler())
              .popChangeHandler(FadeChangeHandler()))
    }

    forumsAdapter.setHasStableIds(true)

    view.refresh_layout.setAppColorScheme()

    with(view.forums_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumsAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      setHasFixedSize(true)
    }

    forumsPresenter.loadForumsList()
  }

  override fun subscribeViews(parent: View) {

    parent.forums_list_refresh_layout?.let {
      RxSwipeRefreshLayout
          .refreshes(parent.forums_list_refresh_layout)
          .autoDisposeWith(scopeProvider)
          .subscribe { forumsPresenter.loadForumsList() }
    }
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    view.forums_list.adapter = null
  }

  override fun clearForumsList() {
    forumsAdapter.clearForumsList()
  }

  override fun appendForumItem(item: ForumItem) {
    forumsAdapter.addForumsListItem(item)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showRefreshing() {
    view?.forums_list_refresh_layout?.isRefreshing = true
  }

  override fun hideRefreshing() {
    view?.forums_list_refresh_layout?.isRefreshing = false
  }

  override fun updateAppbarTitle() {
    view?.context?.let {
      forumsPresenter.updateTitle(it.stringRes(R.string.nav_drawer_forums))
    }
  }
}
