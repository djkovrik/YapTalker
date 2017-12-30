package com.sedsoftware.yaptalker.presentation.features.news

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
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.moveWithAnimationAxisY
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.features.news.adapter.NewsAdapter
import com.sedsoftware.yaptalker.presentation.features.news.adapter.NewsItemElementsClickListener
import com.sedsoftware.yaptalker.presentation.features.news.adapter.NewsItemThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.utility.InfiniteScrollListener
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news.*
import org.jetbrains.anko.browse
import timber.log.Timber
import javax.inject.Inject

class NewsFragment :
    BaseFragment(), NewsView, NewsItemThumbnailsLoader, NewsItemElementsClickListener {

  companion object {
    fun getNewInstance() = NewsFragment()
  }

  override val layoutId: Int
    get() = R.layout.fragment_news

  @Inject
  @InjectPresenter
  lateinit var presenter: NewsPresenter

  @ProvidePresenter
  fun provideNewsPresenter() = presenter

  @Inject
  lateinit var settings: SettingsManager

  private lateinit var newsAdapter: NewsAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    newsAdapter = NewsAdapter(this, this, settings)
    newsAdapter.setHasStableIds(true)

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
    toastError(message)
  }

  override fun appendNewsItem(entity: YapEntity) {
    newsAdapter.addNewsItem(entity)
  }

  override fun clearNewsList() {
    newsAdapter.clearNews()
  }

  override fun updateCurrentUiState() {
    context?.stringRes(R.string.nav_drawer_main_page)?.let { presenter.setAppbarTitle(it) }
    presenter.setNavDrawerItem(NavigationSection.MAIN_PAGE)
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

  override fun onNewsItemClick(forumId: Int, topicId: Int) {
    presenter.navigateToChosenTopic(Triple(forumId, topicId, 0))
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

  override fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean) {
    when {
      isVideo && url.contains("youtube") -> {
        val videoId = url.extractYoutubeVideoId()
        context?.browse("http://www.youtube.com/watch?v=$videoId")
      }
      isVideo && !url.contains("youtube") -> {
        presenter.navigateToChosenVideo(html)
      }
      else -> {
        presenter.navigateToChosenImage(url)
      }
    }
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
