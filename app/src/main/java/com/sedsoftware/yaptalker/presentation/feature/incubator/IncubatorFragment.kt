package com.sedsoftware.yaptalker.presentation.feature.incubator

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.custom.InfiniteScrollListener
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.moveWithAnimationAxisY
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.feature.incubator.adapter.IncubatorAdapter
import com.sedsoftware.yaptalker.presentation.model.base.IncubatorItemModel
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_incubator.*
import timber.log.Timber
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_incubator)
class IncubatorFragment : BaseFragment(), IncubatorView, ThumbnailsProvider {

    companion object {
        fun getNewInstance() = IncubatorFragment()
    }

    @Inject
    lateinit var incubatorAdapter: IncubatorAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: IncubatorPresenter

    @ProvidePresenter
    fun provideNewsPresenter() = presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(incubator_topics_list) {
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            adapter = incubatorAdapter
            setHasFixedSize(true)
            clearOnScrollListeners()

            addOnScrollListener(InfiniteScrollListener({
                presenter.loadIncubator(loadFromFirstPage = false)
            }, linearLayout))
        }

        incubator_refresh_layout.setIndicatorColorScheme()

        subscribeViews()
    }

    override fun showErrorMessage(message: String) {
        messagesDelegate.showMessageError(message)
    }

    override fun showLoadingIndicator() {
        incubator_refresh_layout?.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        incubator_refresh_layout?.isRefreshing = false
    }

    override fun updateCurrentUiState() {
        setCurrentAppbarTitle(string(R.string.nav_drawer_incubator))
        setCurrentNavDrawerItem(NavigationSection.INCUBATOR)
    }

    override fun appendIncubatorItems(items: List<IncubatorItemModel>) {
        incubatorAdapter.addIncubatorItems(items)
    }

    override fun clearIncubatorsList() {
        incubatorAdapter.clearIncubatorItems()
    }

    override fun showFab() {
        incubator_fab?.moveWithAnimationAxisY(offset = 0f)
    }

    override fun hideFab() {
        incubator_fab?.let { fab ->
            val offset = fab.height + fab.paddingTop + fab.paddingBottom
            fab.moveWithAnimationAxisY(offset = offset.toFloat())
        }
    }

    override fun loadThumbnail(videoUrl: String, imageView: ImageView) {
        presenter
            .requestThumbnail(videoUrl)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({ url ->
                if (url.isNotEmpty()) {
                    imageView.loadFromUrl(url)
                } else {
                    context?.let {
                        imageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.ic_othervideo
                            )
                        )
                    }
                }
            }, { e: Throwable ->
                Timber.e("Can't load image: ${e.message}")
            })
    }

    private fun subscribeViews() {

        RxSwipeRefreshLayout
            .refreshes(incubator_refresh_layout)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { presenter.loadIncubator(loadFromFirstPage = true) }

        RxRecyclerView
            .scrollEvents(incubator_topics_list)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { event -> presenter.handleFabVisibility(event.dy()) }

        RxView
            .clicks(incubator_fab)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { presenter.loadIncubator(loadFromFirstPage = true) }
    }
}
