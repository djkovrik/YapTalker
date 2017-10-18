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

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val yapDataManager: YapDataManager by instance()
  protected val settings: SettingsHelper by instance()

  // Local presenter lifecycle events channel
  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(PresenterLifecycle.DESTROY)
  }

  fun setAppbarTitle(title: String) {
    viewState.updateAppbarTitle(title)
  }

  protected fun event(@PresenterLifecycle.Event event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
