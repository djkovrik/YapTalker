package com.sedsoftware.yaptalker.features.activetopics

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.parsing.ActiveTopic
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsNavigationPanel
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage
import com.sedsoftware.yaptalker.features.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ActiveTopicsPresenter : BasePresenter<ActiveTopicsView>() {

  companion object {
    private const val ACTIVE_TOPICS_PAGE_KEY = "ACTIVE_TOPICS_PAGE_KEY"
    private const val SEARCH_ID_KEY = "SEARCH_ID_KEY"
    private const val TOPICS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var searchIdKey = ""
  private var currentPage = 1
  private var totalPages = 1

  fun checkSavedState(savedViewState: Bundle?) {
    if (savedViewState != null &&
        savedViewState.containsKey(ACTIVE_TOPICS_PAGE_KEY) &&
        savedViewState.containsKey(SEARCH_ID_KEY)) {

      searchIdKey = savedViewState.getString(SEARCH_ID_KEY)
      onLoadingSuccess(savedViewState.getParcelable(ACTIVE_TOPICS_PAGE_KEY))
    } else {
      refreshTopicsList()
    }
  }

  fun saveCurrentState(outState: Bundle, panel: ActiveTopicsNavigationPanel, topics: List<ActiveTopic>) {
    val activeTopicsPage = ActiveTopicsPage(panel, topics)
    outState.putParcelable(ACTIVE_TOPICS_PAGE_KEY, activeTopicsPage)
    outState.putString(SEARCH_ID_KEY, searchIdKey)
  }

  fun refreshTopicsList() {
    yapDataManager
        .getSearchId()
        .flatMap { searchIdHash ->
          searchIdKey = searchIdHash
          getActiveTopicsPageObservable(searchIdHash)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          activeTopicsPage ->
          onLoadingSuccess(activeTopicsPage)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })

  }

  fun navigateToChosenTopic(pair: Pair<Int, Int>) {
    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, pair)
  }

  fun goToFirstPage() {
    currentPage = 1
    loadActiveTopicsForCurrentPage()
  }

  fun goToLastPage() {
    currentPage = totalPages
    loadActiveTopicsForCurrentPage()
  }

  fun goToPreviousPage() {
    currentPage--
    loadActiveTopicsForCurrentPage()
  }

  fun goToNextPage() {
    currentPage++
    loadActiveTopicsForCurrentPage()
  }

  fun goToChosenPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage
      loadActiveTopicsForCurrentPage()
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  private fun loadActiveTopicsForCurrentPage() {
    getActiveTopicsPageObservable(searchIdKey, currentPage)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          activeTopicsPage ->
          onLoadingSuccess(activeTopicsPage)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun getActiveTopicsPageObservable(searchId: String, page: Int = 1): Single<ActiveTopicsPage> {
    currentPage = page
    val startingTopicNumber = (currentPage - OFFSET_FOR_PAGE_NUMBER) * TOPICS_PER_PAGE

    return yapDataManager
        .getActiveTopics(searchId, startingTopicNumber)
  }

  private fun onLoadingSuccess(page: ActiveTopicsPage) {

    val pageString = page.navigation.currentPage
    val totalPageString = page.navigation.totalPages

    if (pageString.isNotEmpty() && totalPageString.isNotEmpty()) {
      currentPage = pageString.toInt()
      totalPages = totalPageString.toInt()
    }

    viewState.displayActiveTopicsPage(page)
    viewState.scrollToViewTop()
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
