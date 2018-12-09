package com.sedsoftware.yaptalker.presentation.feature.changelog

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.interactor.AppChangelogInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposable
import javax.inject.Inject

@InjectViewState
class ChangelogPresenter @Inject constructor(
    private val changelogInteractor: AppChangelogInteractor,
    private val schedulers: SchedulersProvider
) : BasePresenter<ChangelogView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        changelogInteractor
            .getChangelogText()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ markdown: String ->
                viewState.displayChangelog(markdown)
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }
}
