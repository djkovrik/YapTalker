package com.sedsoftware.yaptalker.features.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.remote.YapDataManager
import com.sedsoftware.yaptalker.features.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import io.reactivex.Maybe
import io.reactivex.subjects.BehaviorSubject

abstract class BasePresenter<View : MvpView> : MvpPresenter<View>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val yapDataManager: YapDataManager by instance()
  protected val settings: SettingsHelper by instance()

  // Presenter lifecycle events channel
  private val lifecycle: BehaviorSubject<Long> = BehaviorSubject.create()

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.onNext(PresenterLifecycle.DESTROY)
  }

  protected fun event(@PresenterLifecycle.LifecycleEvent event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
