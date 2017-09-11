package com.sedsoftware.yaptalker.features.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.remote.YapDataManager
import com.sedsoftware.yaptalker.data.remote.YapRequestState
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

open class BasePresenter<View : MvpView> : MvpPresenter<View>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val yapDataManager: YapDataManager by instance()
  protected val titleChannel: BehaviorRelay<String> by instance()

  // Presenter lifecycle events channel
  private val lifecycle: BehaviorSubject<Long> = BehaviorSubject.create()

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.onNext(BasePresenterLifecycle.DESTROY)
  }

  fun pushAppbarTitle(channel: BehaviorRelay<String>, title: String) {
    Observable.just(title)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(channel)
  }

  protected fun attachRefreshIndicator(requestState: BehaviorRelay<Long>,
      onLoadingStart: () -> Unit, onLoadingFinish: () -> Unit) {

    requestState
        .autoDisposeWith(event(BasePresenterLifecycle.DESTROY))
        .subscribe { state: Long ->
          when (state) {
            YapRequestState.LOADING -> {
              onLoadingStart()
            }
            YapRequestState.COMPLETED,
            YapRequestState.ERROR -> {
              onLoadingFinish()
            }
          }
        }
  }

  protected fun event(@BasePresenterLifecycle.Event event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
