package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpPresenter
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.remote.YapDataManager
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val yapDataManager: YapDataManager by instance()
  protected val settings: SettingsHelper by instance()
  protected val appbarBus: BehaviorRelay<String> by instance()

  // Local presenter lifecycle events channel
  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(PresenterLifecycle.DESTROY)
  }

  fun updateAppbarTitle(title: String) {
    Observable
        .just(title)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(appbarBus)
  }

  protected fun event(@PresenterLifecycle.Event event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
