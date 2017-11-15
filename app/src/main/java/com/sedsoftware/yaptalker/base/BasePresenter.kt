package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpPresenter
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.base.events.ConnectionState
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems
import com.sedsoftware.yaptalker.data.YapDataManager
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.terrakok.cicerone.Router

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val router: Router by instance()
  protected val yapDataManager: YapDataManager by instance()
  protected val settings: SettingsHelper by instance()
  protected val appbarRelay: BehaviorRelay<String> by instance()
  protected val navDrawerRelay: BehaviorRelay<Long> by instance("navDrawer")
  private val connectionRelay: BehaviorRelay<Long> by instance("connectionState")

  // Local presenter lifecycle events channel
  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    connectionRelay
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { event ->
          when (event) {
            ConnectionState.LOADING -> onLoadingStart()
            ConnectionState.COMPLETED -> onLoadingFinish()
            ConnectionState.ERROR -> onLoadingFinish()
          }
        }
  }

  override fun attachView(view: View?) {
    super.attachView(view)
    viewState.highlightCurrentNavDrawerItem()
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(PresenterLifecycle.DESTROY)
  }

  fun setAppbarTitle(title: String) {
    Observable
        .just(title)
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe(appbarRelay)
  }

  fun setNavDrawerItem(@NavigationDrawerItems.Section item: Long) {
    Observable
        .just(item)
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe(navDrawerRelay)
  }

  protected fun event(@PresenterLifecycle.Event event: Long): Maybe<*> =
      lifecycle.filter({ e -> e == event }).firstElement()

  private fun onLoadingStart() {
    viewState.showLoadingIndicator()
  }

  private fun onLoadingFinish() {
    viewState.hideLoadingIndicator()
  }
}
