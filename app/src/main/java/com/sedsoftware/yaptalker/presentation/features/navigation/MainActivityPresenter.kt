package com.sedsoftware.yaptalker.presentation.features.navigation

import android.text.Spanned
import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.domain.interactor.GetEulaText
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.commons.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.mappers.util.TextTransformer
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val appbarRelay: BehaviorRelay<String>,
    private val navDrawerRelay: BehaviorRelay<Long>,
    private val settings: SettingsManager,
    private val getEulaTextUseCase: GetEulaText,
    private val textTransformer: TextTransformer
) : BasePresenter<MainActivityView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    // TODO() Add event emitting functions to base classes
    appbarRelay
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe { title ->
          viewState.setAppbarTitle(title)
        }

    navDrawerRelay
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe { item ->
          viewState.selectNavDrawerItem(item)
        }

    if (!settings.isEulaAccepted()) {
      requestForEulaDisplaying()
    }
  }

  fun markEulaAsAccepted() {
    settings.markEulaAccepted()
  }

  private fun requestForEulaDisplaying() {
    getEulaTextUseCase
        .buildUseCaseObservable(Unit)
        .subscribeOn(Schedulers.io())
        .map { text: String -> textTransformer.transformHtmlToSpanned(text) }
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getParsedTextObserver())
  }

  private fun getParsedTextObserver() =
      object : DisposableObserver<Spanned>() {
        override fun onNext(text: Spanned) {
          viewState.displayFormattedEulaText(text)
        }

        override fun onComplete() {
          Timber.i("EULA text displayed.")
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }
}
