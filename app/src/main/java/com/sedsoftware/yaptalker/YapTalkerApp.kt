package com.sedsoftware.yaptalker

import android.app.Application
import com.facebook.stetho.Stetho
import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class YapTalkerApp : Application() {

  companion object {
    lateinit var appComponent: ApplicationComponent
  }

  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return
    }

    LeakCanary.install(this)

    Timber.uprootAll()

    if (BuildConfig.DEBUG) {
      LeakCanary.install(this)
      Stetho.initializeWithDefaults(this)
      Timber.plant(Timber.DebugTree())
    } else {
      // TODO() Init Timber with Firebase Crash Reporting tree here
    }

    appComponent = DaggerApplicationComponent.builder().build()
  }
}