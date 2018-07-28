package com.sedsoftware.yaptalker.presentation.feature.changelog

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.ChangelogInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ChangelogPresenter @Inject constructor(
    private val changelogInteractor: ChangelogInteractor
) : BasePresenter<ChangelogView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        changelogInteractor
            .getChangelogText()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoadingIndicator() }
            .doFinally { viewState.hideLoadingIndicator() }
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ markdown ->
                viewState.displayChangelog(markdown)
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }
}
