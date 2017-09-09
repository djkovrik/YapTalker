package com.sedsoftware.yaptalker.features.base

import android.support.annotation.NonNull
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.remote.YapDataManager
import com.sedsoftware.yaptalker.data.remote.YapRequestState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<View : MvpView> : MvpPresenter<View>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val yapDataManager: YapDataManager by instance()
  protected val titleChannel: BehaviorRelay<String> by instance()

  private val subscriptions by lazy { CompositeDisposable() }

  override fun onDestroy() {
    super.onDestroy()
    subscriptions.clear()
  }

  fun pushAppbarTitle(channel: BehaviorRelay<String>, title: String) {
    Observable.just(title)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(channel)
  }

  protected fun unsubscribeOnDestroy(@NonNull subscription: Disposable) {
    subscriptions.add(subscription)
  }

  protected fun attachRefreshIndicator(requestState: BehaviorRelay<Long>,
      onLoadingStart: () -> Unit, onLoadingFinish: () -> Unit) {

    requestState
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
        .apply { unsubscribeOnDestroy(this) }
  }
}
