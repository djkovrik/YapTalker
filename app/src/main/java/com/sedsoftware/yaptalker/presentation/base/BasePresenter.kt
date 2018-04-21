package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.base.event.AppEvent
import com.sedsoftware.yaptalker.presentation.base.event.AppEvent.AppbarEvent
import com.sedsoftware.yaptalker.presentation.base.event.AppEvent.NavDrawerEvent
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

abstract class BasePresenter<View : MvpView> : MvpPresenter<View>() {

  @Inject
  lateinit var eventBus: BehaviorRelay<AppEvent>

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

  fun setAppbarTitle(title: String) {
    Observable
      .just(AppbarEvent(title))
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(eventBus)
  }

  fun setNavDrawerItem(@NavigationSection.Section section: Long) {
    Observable
      .just(NavDrawerEvent(section))
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(eventBus)
  }

  protected fun event(@PresenterLifecycle.Event event: Long): Maybe<*> =
    lifecycle.filter({ e -> e == event }).firstElement()
}
