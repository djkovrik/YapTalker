package com.sedsoftware.yaptalker

import android.app.Application
import com.facebook.stetho.Stetho
import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class YapTalkerAppDebug : Application() {

  companion object {
    lateinit var appComponent: ApplicationComponent
  }

  override fun onCreate() {
    super.onCreate()

    Timber.uprootAll()
    Timber.plant(Timber.DebugTree())

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return
    }

    LeakCanary.install(this)

    Stetho.initializeWithDefaults(this)

    appComponent = DaggerApplicationComponent.builder().build()
  }
}