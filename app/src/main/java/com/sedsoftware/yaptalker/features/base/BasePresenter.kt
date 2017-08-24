package com.sedsoftware.yaptalker.features.base

import android.support.annotation.NonNull
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.remote.yap.YapRequestState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

  private val subscriptions by lazy { CompositeDisposable() }

  protected fun unsubscribeOnDestroy(@NonNull subscription: Disposable) {
    subscriptions.add(subscription)
  }

  protected fun attachRefreshIndicator(requestState: BehaviorRelay<Long>,
      onLoadingStart: () -> Unit, onLoadingFinish: () -> Unit) {

    val subscription =
        requestState.subscribe { state: Long ->
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

    unsubscribeOnDestroy(subscription)
  }

  override fun onDestroy() {
    super.onDestroy()
    subscriptions.clear()
  }
}
