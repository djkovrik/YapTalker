package com.sedsoftware.yaptalker.presentation.feature.incubator

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.IncubatorInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.feature.incubator.adapter.IncubatorElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.IncubatorModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.IncubatorItemModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Single
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class IncubatorPresenter @Inject constructor(
    private val router: Router,
    private val settings: Settings,
    private val incubatorInteractor: IncubatorInteractor,
    private val videoThumbnailsInteractor: VideoThumbnailsInteractor,
    private val incubatorModelMapper: IncubatorModelMapper,
    private val schedulers: SchedulersProvider
) : BasePresenter<IncubatorView>(), IncubatorElementsClickListener {

    companion object {
        private const val ITEMS_PER_PAGE = 50
    }

    private var currentPage = 0
    private var backToFirstPage = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadIncubator(loadFromFirstPage = true)
    }

    override fun attachView(view: IncubatorView?) {
        super.attachView(view)
        viewState.updateCurrentUiState()
    }

    override fun onIncubatorItemClicked(forumId: Int, topicId: Int) {
        router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, Triple(forumId, topicId, 0))
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

    fun loadIncubator(loadFromFirstPage: Boolean) {

        backToFirstPage = loadFromFirstPage

        if (backToFirstPage) {
            currentPage = 0
        } else {
            currentPage += ITEMS_PER_PAGE
        }

        loadDataForCurrentPage()
    }

    private fun loadDataForCurrentPage() {
        incubatorInteractor
            .getIncubatorPage(currentPage)
            .map(incubatorModelMapper)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getIncubatorObserver())
    }

    private fun getIncubatorObserver() =
        object : DisposableObserver<IncubatorItemModel>() {

            override fun onNext(item: IncubatorItemModel) {
                if (backToFirstPage) {
                    viewState.clearIncubatorsList()
                    backToFirstPage = false
                }

                viewState.appendIncubatorItem(item)
            }

            override fun onComplete() {
                Timber.i("Incubator page loading completed.")
            }

            override fun onError(e: Throwable) {
                e.message?.let { viewState.showErrorMessage(it) }
            }
        }
}
