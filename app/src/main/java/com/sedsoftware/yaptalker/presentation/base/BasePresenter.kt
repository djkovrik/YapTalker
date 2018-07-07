package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import io.reactivex.Maybe

abstract class BasePresenter<View : MvpView> : MvpPresenter<View>() {

    private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

    init {
        lifecycle.accept(PresenterLifecycle.CREATE)
    }

    override fun attachView(view: View?) {
        super.attachView(view)
        lifecycle.accept(PresenterLifecycle.ATTACH_VIEW)
    }

    override fun detachView(view: View?) {
        super.detachView(view)
        lifecycle.accept(PresenterLifecycle.DETACH_VIEW)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.accept(PresenterLifecycle.DESTROY)
    }

    protected fun event(@PresenterLifecycle.Event event: Long): Maybe<*> =
        lifecycle.filter({ e -> e == event }).firstElement()
}
