package com.sedsoftware.yaptalker.presentation.feature.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.BlacklistInteractor
import com.sedsoftware.yaptalker.domain.interactor.NewsInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.feature.news.adapter.NewsItemElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.NewsModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class NewsPresenter @Inject constructor(
  private val router: Router,
  private val settings: Settings,
  private val newsInteractor: NewsInteractor,
  private val videoThumbnailsInteractor: VideoThumbnailsInteractor,
  private val blacklistInteractor: BlacklistInteractor,
  private val newsModelMapper: NewsModelMapper
) : BasePresenter<NewsView>(), NewsItemElementsClickListener {

  companion object {
    private const val NEWS_PER_PAGE = 50
  }

  private var currentPage = 0
  private var backToFirstPage = false
  private lateinit var currentNewsItem: NewsItemModel

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadNews(loadFromFirstPage = true)
  }

  override fun attachView(view: NewsView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  override fun onNewsItemClicked(forumId: Int, topicId: Int) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, Triple(forumId, topicId, 0))
  }

  override fun onNewsItemLongClicked(item: NewsItemModel) {
    currentNewsItem = item
    viewState.showBlacklistRequest()
  }

  override fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean) {
    when {
      isVideo && url.contains("youtube") -> {
        val videoId = url.extractYoutubeVideoId()
        viewState.browseExternalResource("http://www.youtube.com/watch?v=$videoId")
      }

      isVideo && url.contains("coub") && settings.isExternalCoubPlayer() -> {
        viewState.browseExternalResource(url.validateUrl())
      }

      isVideo && !url.contains("youtube") -> {
        router.navigateTo(NavigationScreen.VIDEO_DISPLAY_SCREEN, html)
      }

      else -> {
        router.navigateTo(NavigationScreen.IMAGE_DISPLAY_SCREEN, url)
      }
    }
  }

  fun addSelectedTopicToBlacklist() {
    blacklistInteractor
      .addTopicToBlacklist(currentNewsItem.title, currentNewsItem.topicId)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({
        Timber.i("Current topic added to blacklist.")
        viewState.showTopicBlacklistedMessage()
        viewState.removeBlacklistedTopicFromList(currentNewsItem)
      }, { error ->
        error.message?.let { viewState.showErrorMessage(it) }
      })
  }

  fun handleFabVisibility(diff: Int) {
    when {
      diff > 0 -> viewState.hideFab()
      diff < 0 -> viewState.showFab()
    }
  }

  fun requestThumbnail(videoUrl: String): Single<String> =
    videoThumbnailsInteractor
      .getThumbnail(videoUrl)

  fun loadNews(loadFromFirstPage: Boolean) {

    backToFirstPage = loadFromFirstPage

    if (backToFirstPage) {
      currentPage = 0
    } else {
      currentPage += NEWS_PER_PAGE
    }

    loadDataForCurrentPage()
  }

  fun navigateToChosenVideo(html: String) {
    router.navigateTo(NavigationScreen.VIDEO_DISPLAY_SCREEN, html)
  }

  fun navigateToChosenImage(url: String) {
    router.navigateTo(NavigationScreen.IMAGE_DISPLAY_SCREEN, url)
  }

  private fun loadDataForCurrentPage() {
    newsInteractor
      .getNewsPage(currentPage)
      .subscribeOn(Schedulers.io())
      .map(newsModelMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe { viewState.showLoadingIndicator() }
      .doFinally { viewState.hideLoadingIndicator() }
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getNewsObserver())
  }

  private fun getNewsObserver() =
    object : DisposableObserver<NewsItemModel>() {
      override fun onNext(item: NewsItemModel) {
        if (backToFirstPage) {
          viewState.clearNewsList()
          backToFirstPage = false
        }

        viewState.appendNewsItem(item)
      }

      override fun onComplete() {
        Timber.i("News page loading completed.")
      }

      override fun onError(e: Throwable) {
        e.message?.let { viewState.showErrorMessage(it) }
      }
    }
}
