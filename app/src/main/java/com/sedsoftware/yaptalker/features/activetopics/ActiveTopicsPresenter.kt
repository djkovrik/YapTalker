package com.sedsoftware.yaptalker.features.activetopics

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ActiveTopicsPresenter : BasePresenter<ActiveTopicsView>() {

  companion object {
    private const val TOPICS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var searchIdKey = ""
  private var currentPage = 1
  private var totalPages = 1

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    refreshTopicsList()
  }

  override fun attachView(view: ActiveTopicsView?) {
    super.attachView(view)
    viewState.updateAppbarTitle()
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
          onLoadingSuccess(activeTopicsPage, scrollToTop = false)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })

  }

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun goToFirstPage() {
    currentPage = 1
    loadActiveTopicsForCurrentPage(scrollToTop = true)
  }

  fun goToLastPage() {
    currentPage = totalPages
    loadActiveTopicsForCurrentPage(scrollToTop = true)
  }

  fun goToPreviousPage() {
    currentPage--
    loadActiveTopicsForCurrentPage(scrollToTop = true)
  }

  fun goToNextPage() {
    currentPage++
    loadActiveTopicsForCurrentPage(scrollToTop = true)
  }

  fun goToChosenPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage
      loadActiveTopicsForCurrentPage(scrollToTop = true)
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  private fun loadActiveTopicsForCurrentPage(scrollToTop: Boolean) {
    getActiveTopicsPageObservable(searchIdKey, currentPage)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          activeTopicsPage ->
          onLoadingSuccess(activeTopicsPage, scrollToTop)
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

  private fun onLoadingSuccess(page: ActiveTopicsPage, scrollToTop: Boolean) {

    val pageString = page.navigation.currentPage
    val totalPageString = page.navigation.totalPages

    if (pageString.isNotEmpty() && totalPageString.isNotEmpty()) {
      currentPage = pageString.toInt()
      totalPages = totalPageString.toInt()
    }

    viewState.displayActiveTopicsPage(page)

    if (scrollToTop) {
      viewState.scrollToViewTop()
    }
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { message ->
      viewState.showErrorMessage(message)
    }
  }
}
