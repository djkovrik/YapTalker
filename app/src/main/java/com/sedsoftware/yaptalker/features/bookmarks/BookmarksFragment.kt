package com.sedsoftware.yaptalker.features.bookmarks

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems
import com.sedsoftware.yaptalker.commons.extensions.extractYapIds
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastInfo
import com.sedsoftware.yaptalker.data.parsing.Bookmarks
import com.sedsoftware.yaptalker.features.bookmarks.adapter.BookmarksAdapter
import com.sedsoftware.yaptalker.features.bookmarks.adapter.BookmarksDeleteClickListener
import com.sedsoftware.yaptalker.features.bookmarks.adapter.BookmarksItemClickListener
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_bookmarks.*

class BookmarksFragment : BaseFragment(), BookmarksView, BookmarksItemClickListener, BookmarksDeleteClickListener {

  companion object {
    fun getNewInstance() = BookmarksFragment()
  }

  @InjectPresenter
  lateinit var bookmarksPresenter: BookmarksPresenter

  override val layoutId: Int
    get() = R.layout.fragment_bookmarks

  private lateinit var bookmarksAdapter: BookmarksAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    bookmarksAdapter = BookmarksAdapter(this, this)

    bookmarksAdapter.setHasStableIds(true)

    bookmarks_refresh_layout.setIndicatorColorScheme()

    with(bookmarks_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = bookmarksAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }
  }

  override fun subscribeViews() {
    RxSwipeRefreshLayout
        .refreshes(bookmarks_refresh_layout)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { bookmarksPresenter.loadBookmarks() }
  }

  override fun updateAppbarTitle() {
    context?.stringRes(R.string.nav_drawer_bookmarks)?.let { title ->
      bookmarksPresenter.setAppbarTitle(title)
    }
  }

  override fun highlightCurrentNavDrawerItem() {
    bookmarksPresenter.setNavDrawerItem(NavigationDrawerItems.BOOKMARKS)
  }

  override fun showLoadingIndicator() {
    bookmarks_refresh_layout.isRefreshing = true
  }

  override fun hideLoadingIndicator() {
    bookmarks_refresh_layout.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun displayBookmarks(bookmarks: Bookmarks) {
    bookmarksAdapter.addBookmarks(bookmarks.topics)
  }

  override fun showDeleteConfirmDialog(bookmarkId: Int) {
    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .content(R.string.msg_bookmark_confirm_action)
          .positiveText(R.string.msg_bookmark_confirm_yes)
          .negativeText(R.string.msg_bookmark_confirm_No)
          .onPositive { _, _ -> bookmarksPresenter.onDeleteConfirmed(bookmarkId) }
          .show()
    }
  }

  override fun showBookmarkDeletedMessage() {
    toastInfo(getString(R.string.msg_bookmark_topic_deleted))
  }

  override fun onTopicClick(link: String) {
    val triple = link.extractYapIds()
    if (triple.first != 0) {
      bookmarksPresenter.navigateToBookmarkedTopic(triple)
    }
  }

  override fun onDeleteIconClick(bookmarkId: String) {
    if (bookmarkId.isNotEmpty()) {
      bookmarksPresenter.onDeleteIconClicked(bookmarkId.toInt())
    }
  }
}
