package com.sedsoftware.yaptalker

import android.app.Activity
import android.app.Application
import com.sedsoftware.yaptalker.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class YapTalkerApp : Application(), HasActivityInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    LeakCanary.install(this)

    DaggerAppComponent
        .builder()
        .context(this)
        .build()
        .inject(this)

    initTimber()
  }

  override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

  @Suppress("ConstantConditionIf")
  private fun initTimber() {
    Timber.uprootAll()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
