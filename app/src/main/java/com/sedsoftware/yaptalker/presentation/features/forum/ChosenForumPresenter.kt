package com.sedsoftware.yaptalker.presentation.features.forum

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.device.settings.SettingsManager
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetChosenForum
import com.sedsoftware.yaptalker.domain.interactor.GetChosenForum.Params
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.ConnectionState
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mappers.ForumModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.ForumInfoBlockModel
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ChosenForumPresenter @Inject constructor(
    private val router: Router,
    private val getChosenForumUseCase: GetChosenForum,
    private val forumModelMapper: ForumModelMapper,
    private val preferences: SettingsManager
) : BasePresenter<ChosenForumView>() {

  companion object {
    private const val LAST_UPDATE_SORTER = "last_post"
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var currentForumId = 0
  private var currentSorting = LAST_UPDATE_SORTER
  private var currentPage = 1
  private var totalPages = 1
  private var clearCurrentList = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    viewState.initiateForumLoading()
  }

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun goToFirstPage() {
    currentPage = 1
    loadForumCurrentPage()
  }

  fun goToLastPage() {
    currentPage = totalPages
    loadForumCurrentPage()
  }

  fun goToPreviousPage() {
    currentPage--
    loadForumCurrentPage()
  }

  fun goToNextPage() {
    currentPage++
    loadForumCurrentPage()
  }

  fun goToChosenPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage
      loadForumCurrentPage()
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  fun loadForum(forumId: Int) {
    currentForumId = forumId
    currentPage = 1

    loadForumCurrentPage()
  }

  private fun loadForumCurrentPage() {

    val startingTopic = (currentPage - OFFSET_FOR_PAGE_NUMBER) * preferences.getTopicsPerPage()

    clearCurrentList = true

    getChosenForumUseCase
        .buildUseCaseObservable(Params(currentForumId, startingTopic, currentSorting))
        .subscribeOn(Schedulers.io())
        .map { topics: List<BaseEntity> -> forumModelMapper.transform(topics) }
        .flatMap { topics: List<YapEntity> -> Observable.fromIterable(topics) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { setConnectionState(ConnectionState.LOADING) }
        .doOnError { setConnectionState(ConnectionState.ERROR) }
        .doOnComplete { setConnectionState(ConnectionState.COMPLETED) }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getChosenForumObserver())
  }

  private fun getChosenForumObserver() =
      object : DisposableObserver<YapEntity?>() {

        override fun onNext(item: YapEntity) {
          if (clearCurrentList) {
            clearCurrentList = false
            viewState.clearTopicsList()
          }

          when (item) {
            is ForumInfoBlockModel -> {
              currentForumId = item.forumId
              viewState.updateCurrentUiState(item.forumTitle)
            }
            is NavigationPanelModel -> {
              currentPage = item.currentPage
              totalPages = item.totalPages
              viewState.addTopicItem(item)
            }
            else -> {
              viewState.addTopicItem(item)
            }
          }
        }

        override fun onComplete() {
          Timber.i("Forum page loading completed.")
          viewState.scrollToViewTop()
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }
}
