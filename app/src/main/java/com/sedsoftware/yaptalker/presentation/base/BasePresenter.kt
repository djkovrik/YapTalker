package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.commons.enums.ConnectionState
import com.sedsoftware.yaptalker.commons.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.data.event.AppEvent
import com.sedsoftware.yaptalker.data.event.AppEvent.ConnectionEvent
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

  @Inject
  lateinit var eventBus: BehaviorRelay<AppEvent>

  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  init {
    lifecycle.accept(PresenterLifecycle.CREATE)
  }

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    eventBus
        .filter { event -> event is ConnectionEvent }
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe({ event ->
          event as ConnectionEvent
          when (event.state) {
            ConnectionState.LOADING -> viewState.showLoadingIndicator()
            else -> viewState.hideLoadingIndicator()
          }
        }, { throwable ->
          Timber.e("Error while handling app event: ${throwable.message}")
        })
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(PresenterLifecycle.DESTROY)
  }

  protected fun event(@PresenterLifecycle.Event event: Long): Maybe<*> =
      lifecycle.filter({ e -> e == event }).firstElement()
}
