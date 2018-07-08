package com.sedsoftware.yaptalker.presentation.feature.forumslist

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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
import com.sedsoftware.yaptalker.presentation.feature.forumslist.adapter.ForumsAdapter
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_forums_list.*
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_forums_list)
class ForumsFragment : BaseFragment(), ForumsView {

    companion object {
        fun getNewInstance() = ForumsFragment()
    }

    @Inject
    lateinit var forumsAdapter: ForumsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: ForumsPresenter

    @ProvidePresenter
    fun provideForumsPresenter() = presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(forums_list) {
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            adapter = forumsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }

        forums_list_refresh_layout.setIndicatorColorScheme()

        subscribeViews()
    }

    override fun showLoadingIndicator() {
        forums_list_refresh_layout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        forums_list_refresh_layout.isRefreshing = false
    }

    override fun showErrorMessage(message: String) {
        messagesDelegate.showMessageError(message)
    }

    override fun updateCurrentUiState() {
        setCurrentAppbarTitle(string(R.string.nav_drawer_forums))
        setCurrentNavDrawerItem(NavigationSection.FORUMS)
    }

    override fun appendForumItem(item: ForumModel) {
        forumsAdapter.addForumsListItem(item)
    }

    override fun clearForumsList() {
        forumsAdapter.clearForumsList()
    }

    private fun subscribeViews() {
        RxSwipeRefreshLayout
            .refreshes(forums_list_refresh_layout)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { presenter.loadForumsList() }
    }
}
