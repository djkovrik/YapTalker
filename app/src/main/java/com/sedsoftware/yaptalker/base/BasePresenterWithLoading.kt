package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.remote.YapDataManager
import com.sedsoftware.yaptalker.base.events.ConnectionState
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Maybe
import io.reactivex.subjects.BehaviorSubject

abstract class BasePresenterWithLoading<View : MvpView> : MvpPresenter<View>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val yapDataManager: YapDataManager by instance()
  protected val settings: SettingsHelper by instance()

  // Connection events channel
  private val connectionRelay: BehaviorRelay<Long> by instance()

  // Presenter lifecycle events channel
  private val lifecycle: BehaviorSubject<Long> = BehaviorSubject.create()

  abstract fun onLoadingStarted()
  abstract fun onLoadingCompleted()
  abstract fun onLoadingError()

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    connectionRelay
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { event ->
          when (event) {
            ConnectionState.LOADING -> onLoadingStarted()
            ConnectionState.COMPLETED -> onLoadingCompleted()
            ConnectionState.ERROR -> onLoadingError()
          }
        }
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.onNext(PresenterLifecycle.DESTROY)
  }

  protected fun event(@PresenterLifecycle.LifecycleEvent event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
