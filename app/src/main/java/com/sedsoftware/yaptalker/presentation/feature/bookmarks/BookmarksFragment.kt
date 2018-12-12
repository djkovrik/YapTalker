package com.sedsoftware.yaptalker.presentation.feature.bookmarks

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter.BookmarksAdapter
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_bookmarks.bookmarks_list
import kotlinx.android.synthetic.main.fragment_bookmarks.bookmarks_refresh_layout
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_bookmarks)
class BookmarksFragment : BaseFragment(), BookmarksView {

    companion object {
        fun getNewInstance(): BookmarksFragment = BookmarksFragment()
    }

    @Inject
    lateinit var bookmarksAdapter: BookmarksAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: BookmarksPresenter

    @ProvidePresenter
    fun provideBookmarksPresenter() = presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        messagesDelegate.showMessageError(message)
    }

    override fun updateCurrentUiState() {
        setCurrentAppbarTitle(string(R.string.nav_drawer_bookmarks))
        setCurrentNavDrawerItem(NavigationSection.BOOKMARKS)
    }


    override fun appendBookmarkItem(item: BookmarkedTopicModel) {
        bookmarksAdapter.addBookmarkItem(item)
    }

    override fun clearBookmarksList() {
        bookmarksAdapter.clearBookmarksList()
    }

    override fun showDeleteConfirmationDialog(item: BookmarkedTopicModel) {
        context?.let { ctx ->
            MaterialDialog.Builder(ctx)
                .content(R.string.msg_bookmark_confirm_action)
                .positiveText(R.string.msg_bookmark_confirm_yes)
                .negativeText(R.string.msg_bookmark_confirm_no)
                .onPositive { _, _ -> presenter.deleteSelectedBookmark(item) }
                .show()
        }
    }

    override fun showBookmarkDeletedMessage() {
        messagesDelegate.showMessageInfo(getString(R.string.msg_bookmark_topic_deleted))
    }

    private fun subscribeViews() {

        RxSwipeRefreshLayout.refreshes(bookmarks_refresh_layout)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({
                presenter.loadBookmarks()
            }, { e: Throwable ->
                e.message?.let { showErrorMessage(it) }
            })
    }
}
