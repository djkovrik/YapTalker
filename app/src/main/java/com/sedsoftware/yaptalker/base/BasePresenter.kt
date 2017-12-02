package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpPresenter
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import io.reactivex.Maybe

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  init {
    lifecycle.accept(PresenterLifecycle.CREATE)
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(PresenterLifecycle.DESTROY)
  }

  protected fun event(@PresenterLifecycle.Event event: Long): Maybe<*> =
      lifecycle.filter({ e -> e == event }).firstElement()
}
