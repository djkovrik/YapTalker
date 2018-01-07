package com.sedsoftware.yaptalker.presentation.base

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.ActivityLifecycle
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Maybe
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

abstract class BaseActivity : MvpAppCompatActivity(), HasSupportFragmentInjector, BackPressHandler {

  @Inject
  lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

  @Inject
  lateinit var navigatorHolder: NavigatorHolder

  protected abstract val layoutId: Int
  protected lateinit var backPressFragment: BaseFragment
  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    applyTheme()
    super.onCreate(savedInstanceState)

    setContentView(layoutId)

    lifecycle.accept(ActivityLifecycle.CREATE)
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

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

  override fun setSelectedFragment(fragment: BaseFragment) {
    backPressFragment = fragment
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
