package com.sedsoftware.yaptalker.presentation.feature.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import androidx.core.os.bundleOf
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.custom.InfiniteScrollListener
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.feature.search.adapter.SearchResultsAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_site_search_results.*
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_site_search_results)
class SearchResultsFragment : BaseFragment(), SearchResultsView {

    companion object {
        fun getNewInstance(request: SearchRequest): SearchResultsFragment =
            SearchResultsFragment().apply { arguments = bundleOf(SEARCH_REQUEST_KEY to request) }

        private const val SEARCH_REQUEST_KEY = "SEARCH_REQUEST_KEY"
    }

    @Inject
    lateinit var searchResultsAdapter: SearchResultsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: SearchResultsPresenter

    @ProvidePresenter
    fun provideSearchResultsPresenter() = presenter

    private val searchRequest: SearchRequest by lazy {
        arguments?.getParcelable(SEARCH_REQUEST_KEY) as SearchRequest
    }

    private val searchInTags: Boolean by lazy {
        searchRequest.searchInTags
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(search_results_list) {
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            adapter = searchResultsAdapter
            setHasFixedSize(true)
            clearOnScrollListeners()

            addOnScrollListener(
                InfiniteScrollListener(
                    func = { presenter.loadNextSearchResultsPage() },
                    layoutManager = linearLayout,
                    visibleThreshold = 6
                )
            )
        }

        search_refresh_layout.setIndicatorColorScheme()

        RxSwipeRefreshLayout
            .refreshes(search_refresh_layout)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { search_refresh_layout?.isRefreshing = false }

        if (searchInTags) {
            presenter.searchInTags(searchRequest.searchFor)
        } else {
            presenter.searchForFirstTime(searchRequest)
        }
    }

    override fun showErrorMessage(message: String) {
        messagesDelegate.showMessageError(message)
    }

    override fun showLoadingIndicator() {
        search_refresh_layout?.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        search_refresh_layout?.isRefreshing = false
    }

    override fun updateCurrentUiState() {
        setCurrentAppbarTitle(searchRequest.searchFor)
        setCurrentNavDrawerItem(NavigationSection.SITE_SEARCH)
    }

    override fun appendSearchResultsTopicItem(item: DisplayedItemModel) {
        searchResultsAdapter.addResultsItem(item)
    }
}
