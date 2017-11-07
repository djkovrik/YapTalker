package com.sedsoftware.yaptalker.features.bookmarks

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.commons.extensions.extractYapIds
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastInfo
import com.sedsoftware.yaptalker.data.parsing.Bookmarks
import com.sedsoftware.yaptalker.features.bookmarks.adapter.BookmarksAdapter
import com.sedsoftware.yaptalker.features.bookmarks.adapter.BookmarksDeleteClickListener
import com.sedsoftware.yaptalker.features.bookmarks.adapter.BookmarksItemClickListener
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

    with(bookmarks_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = bookmarksAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    context?.stringRes(R.string.nav_drawer_bookmarks)?.let { bookmarksPresenter.updateAppbarTitle(it) }
  }

//  override fun showLoadingIndicator(shouldShow: Boolean) {
//    if (shouldShow) {
//      bookmarks_list.hideView()
//      bookmarks_loading_indicator.showView()
//    } else {
//      bookmarks_loading_indicator.hideView()
//      bookmarks_list.showView()
//    }
//  }

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
