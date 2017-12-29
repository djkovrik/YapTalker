package com.sedsoftware.yaptalker.presentation.features.activetopics

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetActiveTopicsList
import com.sedsoftware.yaptalker.domain.interactor.GetActiveTopicsList.Params
import com.sedsoftware.yaptalker.domain.interactor.GetSearchId
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.ConnectionState
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mappers.ActiveTopicModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ActiveTopicsPresenter @Inject constructor(
    private val router: Router,
    private val getSearchIdUseCase: GetSearchId,
    private val getActiveTopicsListUseCase: GetActiveTopicsList,
    private val activeTopicsModelMapper: ActiveTopicModelMapper
) : BasePresenter<ActiveTopicsView>() {

  companion object {
    private const val TOPICS_PER_PAGE = 25
    private const val OFFSET_FOR_PAGE_NUMBER = 1
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

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun refreshActiveTopicsList() {

    clearCurrentList = true

    getSearchIdUseCase
        .buildUseCaseObservable(Unit)
        .flatMap { hash: String ->
          searchIdKey = hash
          getActiveTopicsListUseCase.buildUseCaseObservable(Params(hash = searchIdKey, page = 0))
        }
        .subscribeOn(Schedulers.io())
        .map { topics: List<BaseEntity> -> activeTopicsModelMapper.transform(topics) }
        .flatMap { topics: List<YapEntity> -> Observable.fromIterable(topics) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { setConnectionState(ConnectionState.LOADING) }
        .doOnError { setConnectionState(ConnectionState.ERROR) }
        .doOnComplete { setConnectionState(ConnectionState.COMPLETED) }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getActiveTopicsObserver())
  }

  private fun loadActiveTopicsForCurrentPage() {

    val startingTopicNumber = (currentPage - OFFSET_FOR_PAGE_NUMBER) * TOPICS_PER_PAGE
    clearCurrentList = true

    getActiveTopicsListUseCase
        .buildUseCaseObservable(Params(hash = searchIdKey, page = startingTopicNumber))
        .subscribeOn(Schedulers.io())
        .map { item: List<BaseEntity> -> activeTopicsModelMapper.transform(item) }
        .flatMap { list: List<YapEntity> -> Observable.fromIterable(list) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { setConnectionState(ConnectionState.LOADING) }
        .doOnError { setConnectionState(ConnectionState.ERROR) }
        .doOnComplete { setConnectionState(ConnectionState.COMPLETED) }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getActiveTopicsObserver())
  }

  private fun getActiveTopicsObserver() =
      object : DisposableObserver<YapEntity?>() {

        override fun onNext(item: YapEntity) {
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
}
