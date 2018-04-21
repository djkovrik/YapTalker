package com.sedsoftware.yaptalker.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.common.annotation.LayoutResourceTablets
import com.sedsoftware.yaptalker.common.exception.MissingAnnotationException
import com.sedsoftware.yaptalker.domain.device.Settings
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

  @Inject
  lateinit var settings: Settings

  protected lateinit var backPressFragment: BaseFragment

  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    applyTheme()
    super.onCreate(savedInstanceState)

    val clazz = this::class.java

    when {
      clazz.isAnnotationPresent(LayoutResourceTablets::class.java) -> {
        if (settings.isInTwoPaneMode()) {
          setContentView(clazz.getAnnotation(LayoutResourceTablets::class.java).tabletsValue)
        } else {
          setContentView(clazz.getAnnotation(LayoutResourceTablets::class.java).normalValue)
        }
      }

      clazz.isAnnotationPresent(LayoutResource::class.java) -> {
        setContentView(clazz.getAnnotation(LayoutResource::class.java).value)
      }
      else -> {
        throw MissingAnnotationException("$this must be annotated with specific LayoutResource annotation.")
      }
    }

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
    val darkBlack = getString(R.string.pref_appearance_theme_value_dark_black)
    val light = getString(R.string.pref_appearance_theme_value_light)
    val lightContrast = getString(R.string.pref_appearance_theme_value_light_contrast)
    val lightYap = getString(R.string.pref_appearance_theme_value_light_yap)
    val current = settings.getCurrentTheme()

    when (current) {
      dark -> setTheme(R.style.AppTheme_Dark)
      darkBlack -> setTheme(R.style.AppTheme_DarkBlack)
      lightContrast -> setTheme(R.style.AppTheme_LightContrast)
      lightYap -> setTheme(R.style.AppTheme_LightYap)
    }
  }
}
