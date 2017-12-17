package com.sedsoftware.yaptalker.presentation.features.navigation

import android.text.Spanned
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.commons.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.data.event.AppEvent.AppbarEvent
import com.sedsoftware.yaptalker.data.event.AppEvent.NavDrawerEvent
import com.sedsoftware.yaptalker.domain.interactor.GetEulaText
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.mappers.util.TextTransformer
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val settings: SettingsManager,
    private val getEulaTextUseCase: GetEulaText,
    private val textTransformer: TextTransformer
) : BasePresenter<MainActivityView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    eventBus
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe({ event ->
          when (event) {
            is AppbarEvent -> viewState.setAppbarTitle(event.title)
            is NavDrawerEvent -> viewState.selectNavDrawerItem(event.itemId)
          }
        }, { throwable ->
          Timber.e("Error while handling app event: ${throwable.message}")
        })

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
