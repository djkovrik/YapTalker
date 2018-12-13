package com.sedsoftware.yaptalker.presentation.feature.activetopics

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.interactor.ActiveTopicsInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.feature.activetopics.adapter.ActiveTopicsItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.ActiveTopicModelMapper
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ActiveTopicsPresenter @Inject constructor(
    private val router: Router,
    private val activeTopicsInteractor: ActiveTopicsInteractor,
    private val activeTopicsModelMapper: ActiveTopicModelMapper,
    private val schedulers: SchedulersProvider
) : BasePresenter<ActiveTopicsView>(), ActiveTopicsItemClickListener, NavigationPanelClickListener {

    init {
        router.setResultListener(RequestCode.REFRESH_REQUEST) { loadActiveTopicsForCurrentPage() }
    }

    private var searchIdKey = ""
    private var currentPage = 1
    private var totalPages = 1
    private var clearCurrentList = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshActiveTopicsList()
    }

    override fun attachView(view: ActiveTopicsView?) {
        super.attachView(view)
        viewState.updateCurrentUiState()
    }

    override fun onDestroy() {
        router.removeResultListener(RequestCode.REFRESH_REQUEST)
        super.onDestroy()
    }

    override fun onActiveTopicItemClick(triple: Triple<Int, Int, Int>) {
        router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
    }

    override fun goToFirstPage() {
        currentPage = 1
        loadActiveTopicsForCurrentPage()
    }

    override fun goToLastPage() {
        currentPage = totalPages
        loadActiveTopicsForCurrentPage()
    }

    override fun goToPreviousPage() {
        currentPage--
        loadActiveTopicsForCurrentPage()
    }

    override fun goToNextPage() {
        currentPage++
        loadActiveTopicsForCurrentPage()
    }

    override fun goToSelectedPage() {
        viewState.showPageSelectionDialog()
    }

    fun goToChosenPage(chosenPage: Int) {
        if (chosenPage in 1..totalPages) {
            currentPage = chosenPage
            loadActiveTopicsForCurrentPage()
        } else {
            viewState.showCantLoadPageMessage(chosenPage)
        }
    }

    fun refreshActiveTopicsList() {

        clearCurrentList = true

        activeTopicsInteractor
            .getSearchId()
            .flatMap { hash: String ->
                searchIdKey = hash
                activeTopicsInteractor.getActiveTopics(hash = searchIdKey, page = 0)
            }
            .map(activeTopicsModelMapper)
            .flatMapObservable { Observable.fromIterable(it) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getActiveTopicsObserver())
    }

    private fun loadActiveTopicsForCurrentPage() {

        clearCurrentList = true

        val startingTopicNumber = (currentPage - OFFSET_FOR_PAGE_NUMBER) * TOPICS_PER_PAGE

        activeTopicsInteractor
            .getActiveTopics(hash = searchIdKey, page = startingTopicNumber)
            .map(activeTopicsModelMapper)
            .flatMapObservable { Observable.fromIterable(it) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getActiveTopicsObserver())
    }

    private fun getActiveTopicsObserver() =
        object : DisposableObserver<DisplayedItemModel?>() {

            override fun onNext(item: DisplayedItemModel) {
                if (item is NavigationPanelModel) {
                    currentPage = item.currentPage
                    totalPages = item.totalPages
                }

                if (clearCurrentList) {
                    clearCurrentList = false
                    viewState.clearActiveTopicsList()
                }

                viewState.appendActiveTopicItem(item)
            }

            override fun onComplete() {
                viewState.scrollToViewTop()
            }

            override fun onError(e: Throwable) {
                e.message?.let { viewState.showErrorMessage(it) }
            }
        }

    private companion object {
        const val TOPICS_PER_PAGE = 25
        const val OFFSET_FOR_PAGE_NUMBER = 1
    }
}
