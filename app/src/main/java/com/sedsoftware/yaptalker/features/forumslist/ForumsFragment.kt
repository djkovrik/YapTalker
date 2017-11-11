package com.sedsoftware.yaptalker.features.forumslist

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.parsing.ForumItem
import com.sedsoftware.yaptalker.features.forumslist.adapter.ForumsAdapter
import com.sedsoftware.yaptalker.features.forumslist.adapter.ForumsItemClickListener
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_forums_list.*

class ForumsFragment : BaseFragment(), ForumsView, ForumsItemClickListener {

  companion object {
    fun getNewInstance() = ForumsFragment()
  }

  @InjectPresenter
  lateinit var forumsPresenter: ForumsPresenter

  override val layoutId: Int
    get() = R.layout.fragment_forums_list

  private lateinit var forumsAdapter: ForumsAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    forumsAdapter = ForumsAdapter(this)

    forumsAdapter.setHasStableIds(true)

    forums_list_refresh_layout.setIndicatorColorScheme()

    with(forums_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumsAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      setHasFixedSize(true)
    }
  }

  override fun subscribeViews() {
    RxSwipeRefreshLayout
        .refreshes(forums_list_refresh_layout)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { forumsPresenter.loadForumsList() }
  }

  override fun updateAppbarTitle() {
    context?.stringRes(R.string.nav_drawer_forums)?.let { title ->
      forumsPresenter.setAppbarTitle(title) }
  }

  override fun highlightCurrentNavDrawerItem() {
    forumsPresenter.setNavDrawerItem(NavigationDrawerItems.FORUMS)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator() {
    forums_list_refresh_layout.isRefreshing = true
  }

  override fun hideLoadingIndicator() {
    forums_list_refresh_layout.isRefreshing = false
  }

  override fun appendForumItem(item: ForumItem) {
    forumsAdapter.addForumsListItem(item)
  }

  override fun clearForumsList() {
    forumsAdapter.clearForumsList()
  }

  override fun onForumItemClick(forumId: Int) {
    forumsPresenter.navigateToChosenForum(forumId)
  }
}
