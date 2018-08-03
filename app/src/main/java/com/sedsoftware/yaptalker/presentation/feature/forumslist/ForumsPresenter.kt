package com.sedsoftware.yaptalker.presentation.feature.forumslist

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.interactor.ForumsListInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.feature.forumslist.adapter.ForumsItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.ForumsListModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ForumsPresenter @Inject constructor(
    private val router: Router,
    private val forumsListInteractor: ForumsListInteractor,
    private val forumsListModelMapper: ForumsListModelMapper,
    private val schedulers: SchedulersProvider
) : BasePresenter<ForumsView>(), ForumsItemClickListener {

    private var clearCurrentList = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadForumsList()
    }

    override fun attachView(view: ForumsView?) {
        super.attachView(view)
        viewState.updateCurrentUiState()
    }

    override fun onForumItemClick(forumId: Int, forumTitle: String) {
        router.navigateTo(NavigationScreen.CHOSEN_FORUM_SCREEN, Pair(forumId, forumTitle))
    }

    fun loadForumsList() {

        clearCurrentList = true

        forumsListInteractor
            .getForumsList()
            .map(forumsListModelMapper)
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getForumsListObserver())
    }

    private fun getForumsListObserver() =
        object : DisposableObserver<ForumModel>() {

            override fun onNext(item: ForumModel) {

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
