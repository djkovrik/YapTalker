package com.sedsoftware.yaptalker.presentation.features.bookmarks

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.extensions.extractYapIds
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastInfo
import com.sedsoftware.yaptalker.presentation.features.bookmarks.adapter.BookmarksAdapter
import com.sedsoftware.yaptalker.presentation.features.bookmarks.adapter.BookmarksElementsClickListener
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import javax.inject.Inject

class BookmarksFragment : BaseFragment(), BookmarksView, BookmarksElementsClickListener {

  companion object {
    fun getNewInstance() = BookmarksFragment()
  }

  override val layoutId: Int
    get() = R.layout.fragment_bookmarks

  @Inject
  lateinit var settings: SettingsManager

  @Inject
  @InjectPresenter
  lateinit var presenter: BookmarksPresenter

  @ProvidePresenter
  fun provideBookmarksPresenter() = presenter

  private lateinit var bookmarksAdapter: BookmarksAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    bookmarksAdapter = BookmarksAdapter(this, settings)
    bookmarksAdapter.setHasStableIds(true)


    with(bookmarks_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = bookmarksAdapter
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      setHasFixedSize(true)
    }

    bookmarks_refresh_layout.setIndicatorColorScheme()

    subscribeViews()
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

  override fun appendBookmarkItem(bookmark: YapEntity) {
    bookmarksAdapter.addBookmarkItem(bookmark)
  }

  override fun clearBookmarksList() {
    bookmarksAdapter.clearBookmarksList()
  }

  override fun updateCurrentUiState() {
    context?.stringRes(R.string.nav_drawer_bookmarks)?.let { presenter.setAppbarTitle(it) }
    presenter.setNavDrawerItem(NavigationSection.BOOKMARKS)
  }

  override fun showDeleteConfirmationDialog(bookmarkId: Int) {
    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .content(R.string.msg_bookmark_confirm_action)
          .positiveText(R.string.msg_bookmark_confirm_yes)
          .negativeText(R.string.msg_bookmark_confirm_No)
          .onPositive { _, _ -> presenter.deleteSelectedBookmark(bookmarkId) }
          .show()
    }
  }

  override fun showBookmarkDeletedMessage() {
    toastInfo(getString(R.string.msg_bookmark_topic_deleted))
  }

  override fun onTopicClick(link: String) {
    val triple = link.extractYapIds()
    if (triple.first != 0) {
      presenter.navigateToBookmarkedTopic(triple)
    }
  }

  override fun onDeleteIconClick(bookmarkId: Int) {
    presenter.requestDeleteConfirmationDialog(bookmarkId)
  }

  private fun subscribeViews() {

    RxSwipeRefreshLayout
        .refreshes(bookmarks_refresh_layout)
        .autoDisposable(event(FragmentLifecycle.STOP))
        .subscribe { presenter.loadBookmarks() }
  }
}
