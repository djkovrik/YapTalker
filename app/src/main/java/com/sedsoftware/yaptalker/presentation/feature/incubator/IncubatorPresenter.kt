package com.sedsoftware.yaptalker.presentation.feature.incubator

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.interactor.IncubatorInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.feature.LinkBrowserDelegate
import com.sedsoftware.yaptalker.presentation.feature.incubator.adapter.IncubatorElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.IncubatorModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.IncubatorItemModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class IncubatorPresenter @Inject constructor(
    private val router: Router,
    private val incubatorInteractor: IncubatorInteractor,
    private val videoThumbnailsInteractor: VideoThumbnailsInteractor,
    private val incubatorModelMapper: IncubatorModelMapper,
    private val linksDelegate: LinkBrowserDelegate,
    private val schedulers: SchedulersProvider
) : BasePresenter<IncubatorView>(), IncubatorElementsClickListener {

    companion object {
        private const val ITEMS_PER_PAGE = 50
    }

    private val displayedTopics = mutableSetOf<Int>()
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
        object : SingleObserver<List<IncubatorItemModel>> {

            override fun onSubscribe(d: Disposable) {
                Timber.i("Incubator page loading started.")
            }

            override fun onSuccess(list: List<IncubatorItemModel>) {
                if (backToFirstPage) {
                    viewState.clearIncubatorsList()
                    displayedTopics.clear()
                    backToFirstPage = false
                }

                val loadedTopicIds = list.map { it.topicId }
                val newTopics = list.filter { !displayedTopics.contains(it.topicId) }
                displayedTopics.addAll(loadedTopicIds)

                if (newTopics.isEmpty()) {
                    currentPage += ITEMS_PER_PAGE
                    loadDataForCurrentPage()
                } else {
                    viewState.appendIncubatorItems(newTopics)
                }
            }

            override fun onError(e: Throwable) {
                e.message?.let { viewState.showErrorMessage(it) }
            }
        }
}
