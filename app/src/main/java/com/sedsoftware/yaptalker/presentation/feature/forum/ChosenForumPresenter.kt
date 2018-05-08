package com.sedsoftware.yaptalker.presentation.feature.forum

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.ChosenForumInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.feature.forum.adapter.ChosenForumItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.ForumModelMapper
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
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
  private val forumInteractor: ChosenForumInteractor,
  private val forumModelMapper: ForumModelMapper,
  private val settings: Settings
) : BasePresenter<ChosenForumView>(), ChosenForumItemClickListener, NavigationPanelClickListener {

  companion object {
    private const val LAST_UPDATE_SORTER = "last_post"
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  init {
    router.setResultListener(RequestCode.REFRESH_REQUEST, { loadForumCurrentPage() })
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

  override fun attachView(view: ChosenForumView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  override fun onDestroy() {
    router.removeResultListener(RequestCode.REFRESH_REQUEST)
    super.onDestroy()
  }

  override fun onTopicItemClick(topicId: Int) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, Triple(currentForumId, topicId, 0))
  }

  override fun goToFirstPage() {
    currentPage = 1
    loadForumCurrentPage()
  }

  override fun goToLastPage() {
    currentPage = totalPages
    loadForumCurrentPage()
  }

  override fun goToPreviousPage() {
    currentPage--
    loadForumCurrentPage()
  }

  override fun goToNextPage() {
    currentPage++
    loadForumCurrentPage()
  }

  override fun goToSelectedPage() {
    viewState.showPageSelectionDialog()
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

    val startingTopic = (currentPage - OFFSET_FOR_PAGE_NUMBER) * settings.getTopicsPerPage()

    clearCurrentList = true

    forumInteractor
      .getChosenForum(currentForumId, startingTopic, currentSorting)
      .subscribeOn(Schedulers.io())
      .map(forumModelMapper)
      .flatMap { topics: List<DisplayedItemModel> -> Observable.fromIterable(topics) }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe { viewState.showLoadingIndicator() }
      .doFinally { viewState.hideLoadingIndicator() }
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getChosenForumObserver())
  }

  private fun getChosenForumObserver() =
    object : DisposableObserver<DisplayedItemModel?>() {

      override fun onNext(item: DisplayedItemModel) {
        if (clearCurrentList) {
          clearCurrentList = false
          viewState.clearTopicsList()
        }

        when (item) {
          is ForumInfoBlockModel -> {
            currentForumId = item.forumId
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
