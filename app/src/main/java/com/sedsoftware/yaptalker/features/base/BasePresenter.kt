package com.sedsoftware.yaptalker.features.base

import android.support.annotation.NonNull
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

  private val subscriptions by lazy { CompositeDisposable() }

  protected fun unsubscribeOnDestroy(@NonNull subscription: Disposable) {
    subscriptions.add(subscription)
  }

  override fun onDestroy() {
    super.onDestroy()
    subscriptions.clear()
  }
}
