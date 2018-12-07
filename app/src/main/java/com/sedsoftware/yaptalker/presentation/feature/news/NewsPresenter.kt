package com.sedsoftware.yaptalker.presentation.feature.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.BlacklistInteractor
import com.sedsoftware.yaptalker.domain.interactor.NewsInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.delegate.LinkBrowserDelegate
import com.sedsoftware.yaptalker.presentation.feature.news.adapter.NewsItemElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.NewsModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class NewsPresenter @Inject constructor(
    private val router: Router,
    private val settings: Settings,
    private val targetScreen: String,
    private val newsInteractor: NewsInteractor,
    private val videoThumbnailsInteractor: VideoThumbnailsInteractor,
    private val blacklistInteractor: BlacklistInteractor,
    private val newsModelMapper: NewsModelMapper,
    private val linksDelegate: LinkBrowserDelegate,
    private val schedulers: SchedulersProvider
) : BasePresenter<NewsView>(), NewsItemElementsClickListener {

    companion object {
        private const val NEWS_PER_PAGE = 50
    }

    private val displayedTopics = mutableSetOf<Int>()
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

    override fun onMediaPreviewClicked(url: String, directUrl: String, type: String, html: String, isVideo: Boolean) {
        linksDelegate.checkVideoLink(directUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ link: String ->
                linksDelegate.browse(url, link, type, html, isVideo)
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }

    fun addSelectedTopicToBlacklist() {
        blacklistInteractor
            .addTopicToBlacklist(currentNewsItem.title, currentNewsItem.topicId)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Current topic added to blacklist.")
                viewState.showTopicBlacklistedMessage()
                viewState.removeBlacklistedTopicFromList(currentNewsItem)
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
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
            .observeOn(schedulers.ui())

    fun loadNews(loadFromFirstPage: Boolean) {

        backToFirstPage = loadFromFirstPage

        if (backToFirstPage) {
            currentPage = 0
        } else {
            currentPage += NEWS_PER_PAGE
        }

        loadDataForCurrentPage()
    }

    private fun loadDataForCurrentPage() {
        val url = buildUrl()
        newsInteractor
            .getNewsPage(url)
            .map(newsModelMapper)
            .flatMapObservable { Observable.fromIterable(it) }
            .toList()
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getNewsObserver())
    }

    private fun getNewsObserver() =
        object : SingleObserver<List<NewsItemModel>> {
            override fun onSubscribe(d: Disposable) {
                Timber.i("News page loading started.")
            }

            override fun onSuccess(items: List<NewsItemModel>) {
                if (backToFirstPage) {
                    viewState.clearNewsList()
                    displayedTopics.clear()
                    backToFirstPage = false
                }

                val loadedTopicIds = items.map { it.topicId }
                val newTopics = items.filter { !displayedTopics.contains(it.topicId) }
                displayedTopics.addAll(loadedTopicIds)

                if (newTopics.isEmpty()) {
                    currentPage += NEWS_PER_PAGE
                    loadDataForCurrentPage()
                } else {
                    viewState.appendNewsItems(newTopics)
                }
            }

            override fun onError(e: Throwable) {
                e.message?.let { viewState.showErrorMessage(it) }
            }
        }

    private fun buildUrl(): String {
        val schema = if (settings.isHttpsEnabled()) "https://" else "http://"
        val base = when (targetScreen) {
            NavigationScreen.NEWS_SCREEN -> "www.yaplakal.com/"
            NavigationScreen.PICTURES_SCREEN -> "pics.yaplakal.com/"
            NavigationScreen.VIDEOS_SCREEN -> "video.yaplakal.com/"
            NavigationScreen.EVENTS_SCREEN -> "news.yaplakal.com/"
            NavigationScreen.AUTO_MOTO_SCREEN -> "auto.yaplakal.com/"
            NavigationScreen.ANIMALS_SCREEN -> "animals.yaplakal.com/"
            NavigationScreen.PHOTOBOMB_SCREEN -> "fotozhaba.yaplakal.com/"
            else -> "inkubator.yaplakal.com/"
        }

        return "$schema$base/st/$currentPage/"
    }
}
