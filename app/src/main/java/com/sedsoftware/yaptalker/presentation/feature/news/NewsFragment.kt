package com.sedsoftware.yaptalker.presentation.feature.news

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
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
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.custom.InfiniteScrollListener
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.moveWithAnimationAxisY
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.feature.news.adapter.NewsAdapter
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import com.sedsoftware.yaptalker.presentation.thumbnail.ThumbnailsLoader
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news.*
import org.jetbrains.anko.browse
import timber.log.Timber
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_news)
class NewsFragment : BaseFragment(), NewsView, ThumbnailsLoader {

  companion object {
    fun getNewInstance() = NewsFragment()
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
    setCurrentAppbarTitle(string(R.string.nav_drawer_main_page))
    setCurrentNavDrawerItem(NavigationSection.MAIN_PAGE)
  }

  override fun appendNewsItem(item: NewsItemModel) {
    newsAdapter.addNewsItem(item)
  }

  override fun clearNewsList() {
    newsAdapter.clearNews()
  }

  override fun browseExternalResource(url: String) {
    context?.browse(url.validateUrl())
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
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe({ url ->
        if (url.isNotEmpty()) {
          imageView.loadFromUrl(url)
        } else {
          context?.let { imageView.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_othervideo)) }
        }
      }, { throwable ->
        Timber.e("Can't load image: ${throwable.message}")
      })
  }

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
}