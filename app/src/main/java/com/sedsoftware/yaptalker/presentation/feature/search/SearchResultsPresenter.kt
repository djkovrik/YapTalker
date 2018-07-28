package com.sedsoftware.yaptalker.presentation.feature.search

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.interactor.SearchInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.feature.search.adapter.SearchResultsItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.SearchResultsModelMapper
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.SearchTopicsPageInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class SearchResultsPresenter @Inject constructor(
    private val router: Router,
    private val searchInteractor: SearchInteractor,
    private val searchResultsMapper: SearchResultsModelMapper,
    private val schedulers: SchedulersProvider
) : BasePresenter<SearchResultsView>(), SearchResultsItemClickListener {

    companion object {
        private const val TOPICS_PER_PAGE = 25
    }

    private var searchIdKey = ""
    private var searchKeyword = ""
    private var searchInParam = ""
    private var hasNextPage = false
    private var currentPage = 1

    override fun attachView(view: SearchResultsView?) {
        super.attachView(view)
        viewState.updateCurrentUiState()
    }

    override fun onSearchResultsItemClick(triple: Triple<Int, Int, Int>) {
        router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
    }

    fun searchForFirstTime(request: SearchRequest) {

        searchKeyword = request.searchFor
        searchInParam = request.searchIn

        searchInteractor
            .getSearchResults(
                keyword = request.searchFor,
                searchIn = request.searchIn,
                searchHow = request.searchHow,
                sortBy = request.sortBy,
                targetForums = request.targetForums,
                prune = request.periodInDays.toInt())
            .map(searchResultsMapper)
            .flatMapObservable { Observable.fromIterable(it) }
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getSearchResultsObserver())
    }

    fun searchInTags(tag: String) {
        searchInteractor
            .getTagSearchResults(tag)
            .map(searchResultsMapper)
            .flatMapObservable { Observable.fromIterable(it) }
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getSearchResultsObserver())
    }

    fun loadNextSearchResultsPage() {

        val startingTopicNumber = currentPage * TOPICS_PER_PAGE
        currentPage++

        searchInteractor
            .getSearchResultsNextPage(
                keyword = searchKeyword,
                searchId = searchIdKey,
                searchIn = searchInParam,
                page = startingTopicNumber)
            .map(searchResultsMapper)
            .flatMapObservable { Observable.fromIterable(it) }
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getSearchResultsObserver())
    }

    private fun getSearchResultsObserver() =
        object : DisposableObserver<DisplayedItemModel?>() {

            override fun onNext(item: DisplayedItemModel) {

                if (item is SearchTopicsPageInfoModel) {
                    searchIdKey = if (item.searchId.isNotEmpty()) item.searchId else searchIdKey
                    hasNextPage = item.hasNextPage
                } else {
                    viewState.appendSearchResultsTopicItem(item)
                }
            }

            override fun onComplete() {
                Timber.i("Search results page loading completed.")
            }

            override fun onError(e: Throwable) {
                e.message?.let { viewState.showErrorMessage(it) }
            }
        }
}
