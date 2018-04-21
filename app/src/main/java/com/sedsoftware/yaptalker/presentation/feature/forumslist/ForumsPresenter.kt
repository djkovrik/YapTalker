package com.sedsoftware.yaptalker.presentation.feature.forumslist

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.forumslist.GetForumsList
import com.sedsoftware.yaptalker.presentation.base.BaseLoadingPresenter
import com.sedsoftware.yaptalker.presentation.base.enums.ConnectionState
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mapper.ForumsListModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ForumsPresenter @Inject constructor(
  private val router: Router,
  private val forumsListUseCase: GetForumsList,
  private val forumsListModelMapper: ForumsListModelMapper
) : BaseLoadingPresenter<ForumsView>() {

  private var clearCurrentList = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadForumsList()
  }

  override fun attachView(view: ForumsView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  fun navigateToChosenForum(forumId: Int) {
    router.navigateTo(NavigationScreen.CHOSEN_FORUM_SCREEN, forumId)
  }

  fun loadForumsList() {

    clearCurrentList = true

    forumsListUseCase
      .execute()
      .subscribeOn(Schedulers.io())
      .map(forumsListModelMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe { setConnectionState(ConnectionState.LOADING) }
      .doOnError { setConnectionState(ConnectionState.ERROR) }
      .doOnComplete { setConnectionState(ConnectionState.COMPLETED) }
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getForumsListObserver())
  }

  private fun getForumsListObserver() =
    object : DisposableObserver<YapEntity>() {

      override fun onNext(item: YapEntity) {

        if (clearCurrentList) {
          clearCurrentList = false
          viewState.clearForumsList()
        }

        viewState.appendForumItem(item)
      }

      override fun onComplete() {
        Timber.i("Forums list loading completed.")
      }

      override fun onError(e: Throwable) {
        e.message?.let { viewState.showErrorMessage(it) }
      }
    }
}
