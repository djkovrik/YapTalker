package com.sedsoftware.yaptalker.presentation.features.changelog

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.changelog.GetChangelogText
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ChangelogPresenter @Inject constructor(
  private val getChangelogText: GetChangelogText
) : BasePresenter<ChangelogView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    getChangelogText
      .execute()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ markdown ->
        viewState.displayChangelog(markdown)
      }, { throwable ->
        throwable.message?.let { viewState.showErrorMessage(it) }
      })
  }
}
