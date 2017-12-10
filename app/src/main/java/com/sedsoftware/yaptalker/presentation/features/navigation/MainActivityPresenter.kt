package com.sedsoftware.yaptalker.presentation.features.navigation

import android.text.Spanned
import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.data.task.HtmlToSpannedTask
import com.sedsoftware.yaptalker.data.task.HtmlToSpannedTask.Params
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val appbarRelay: BehaviorRelay<String>,
    private val navDrawerRelay: BehaviorRelay<Long>,
    private val settings: SettingsManager,
    private val parseHtmlTask: HtmlToSpannedTask
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
      viewState.requestEulaDisplaying()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    parseHtmlTask.dispose()
  }

  fun formatEulaHtmlText(eulaText: String) {
    parseHtmlTask.execute(getParsedTextObserver(), Params(html = eulaText))
  }

  fun markEulaAsAccepted() {
    settings.markEulaAccepted()
  }

  private fun getParsedTextObserver() =
      object : DisposableSingleObserver<Spanned>() {
        override fun onSuccess(text: Spanned) {
          viewState.displayFormattedEulaText(text)
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }
}
