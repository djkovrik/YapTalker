package com.sedsoftware.yaptalker.base

import android.os.Bundle
import android.preference.PreferenceManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.base.events.ActivityLifecycle
import io.reactivex.Maybe
import ru.terrakok.cicerone.NavigatorHolder

abstract class BaseActivity : MvpAppCompatActivity(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  protected abstract val layoutId: Int

  // Kodein injections
  protected val navigatorHolder: NavigatorHolder by instance()

  // Local activity lifecycle events channel
  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onCreate(savedInstanceState: Bundle?) {
    applyTheme()
    super.onCreate(savedInstanceState)
    setContentView(layoutId)

    lifecycle.accept(ActivityLifecycle.CREATE)

    subscribeViews()
  }

  @Suppress("EmptyFunctionBlock")
  protected open fun subscribeViews() {
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(ActivityLifecycle.DESTROY)
  }

  override fun onStart() {
    super.onStart()
    lifecycle.accept(ActivityLifecycle.START)
  }

  override fun onStop() {
    super.onStop()
    lifecycle.accept(ActivityLifecycle.STOP)
  }

  override fun onResume() {
    super.onResume()
    lifecycle.accept(ActivityLifecycle.RESUME)
  }

  override fun onPause() {
    super.onPause()
    lifecycle.accept(ActivityLifecycle.PAUSE)
  }

  protected fun event(@ActivityLifecycle.Event event: Long): Maybe<*> =
      lifecycle.filter({ e -> e == event }).firstElement()

  private fun applyTheme() {
    val dark = getString(R.string.pref_appearance_theme_value_dark)
    val light = getString(R.string.pref_appearance_theme_value_light)
    val key = getString(R.string.pref_key_theme)

    PreferenceManager.getDefaultSharedPreferences(this).getString(key, light).let { current ->
      if (dark == current) {
        setTheme(R.style.AppTheme_Dark)
      }
    }
  }
}
