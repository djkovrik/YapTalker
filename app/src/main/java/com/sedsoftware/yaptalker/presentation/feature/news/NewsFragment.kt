package com.sedsoftware.yaptalker.presentation.feature.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.custom.InfiniteScrollListener
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.moveWithAnimationAxisY
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.feature.news.adapter.NewsAdapter
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_news.news_fab
import kotlinx.android.synthetic.main.fragment_news.news_list
import kotlinx.android.synthetic.main.fragment_news.refresh_layout
import timber.log.Timber
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_news)
class NewsFragment : BaseFragment(), NewsView, ThumbnailsProvider {

    companion object {
        fun getNewInstance(target: String) = NewsFragment().apply {
            arguments = bundleOf(TARGET_ID to target)
        }

        private const val TARGET_ID = "TARGET_ID"
    }

    val targetScreen: String by lazy {
        arguments?.getString(TARGET_ID) ?: ""
    }

    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: NewsPresenter

    @ProvidePresenter
    fun provideNewsPresenter() = presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(news_list) {
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            adapter = newsAdapter
            setHasFixedSize(true)
            clearOnScrollListeners()

            addOnScrollListener(InfiniteScrollListener({
                presenter.loadNews(loadFromFirstPage = false)
            }, linearLayout))
        }

        refresh_layout.setIndicatorColorScheme()

        subscribeViews()
    }

    override fun showErrorMessage(message: String) {
        messagesDelegate.showMessageError(message)
    }

    override fun updateCurrentUiState() {
        setCurrentAppbarTitle(buildTitle())
        setCurrentNavDrawerItem(NavigationSection.MAIN_PAGE)
    }

    override fun appendNewsItems(items: List<NewsItemModel>) {
        newsAdapter.addNewsItems(items)
    }

    override fun clearNewsList() {
        newsAdapter.clearNews()
    }

    override fun showLoadingIndicator() {
        refresh_layout?.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        refresh_layout?.isRefreshing = false
    }

    override fun showFab() {
        news_fab?.moveWithAnimationAxisY(offset = 0f)
    }

    override fun hideFab() {
        news_fab?.let { fab ->
            val offset = fab.height + fab.paddingTop + fab.paddingBottom
            fab.moveWithAnimationAxisY(offset = offset.toFloat())
        }
    }

    override fun showBlacklistRequest() {
        context?.let { ctx ->
            MaterialDialog.Builder(ctx)
                .content(R.string.msg_blacklist_request)
                .positiveText(R.string.msg_blacklist_confirm_yes)
                .negativeText(R.string.msg_blacklist_confirm_no)
                .onPositive { _, _ -> presenter.addSelectedTopicToBlacklist() }
                .show()
        }
    }

    override fun showTopicBlacklistedMessage() {
        context?.string(R.string.msg_blacklist_added)?.let { message ->
            messagesDelegate.showMessageInfo(message)
        }
    }

    override fun removeBlacklistedTopicFromList(topic: NewsItemModel) {
        newsAdapter.removeNewsItem(topic)
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

    @SuppressLint("RxSubscribeOnError")
    private fun subscribeViews() {

        RxSwipeRefreshLayout
            .refreshes(refresh_layout)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { presenter.loadNews(loadFromFirstPage = true) }

        RxRecyclerView
            .scrollEvents(news_list)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { event -> presenter.handleFabVisibility(event.dy()) }

        RxView
            .clicks(news_fab)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe { presenter.loadNews(loadFromFirstPage = true) }
    }

    private fun buildTitle(): String =
        when (targetScreen) {
            NavigationScreen.NEWS_SCREEN -> string(R.string.nav_drawer_main_page)
            NavigationScreen.PICTURES_SCREEN -> string(R.string.nav_drawer_pictures)
            NavigationScreen.VIDEOS_SCREEN -> string(R.string.nav_drawer_video)
            NavigationScreen.EVENTS_SCREEN -> string(R.string.nav_drawer_events)
            NavigationScreen.AUTO_MOTO_SCREEN -> string(R.string.nav_drawer_auto_moto)
            NavigationScreen.ANIMALS_SCREEN -> string(R.string.nav_drawer_animals)
            NavigationScreen.PHOTOBOMB_SCREEN -> string(R.string.nav_drawer_photobomb)
            else -> string(R.string.nav_drawer_incubator)
        }
}
