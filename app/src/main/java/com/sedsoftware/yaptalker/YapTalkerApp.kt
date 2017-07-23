package com.sedsoftware.yaptalker

import android.app.Application
import timber.log.Timber

class YapTalkerApp : Application() {

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.uprootAll()
      Timber.plant(Timber.DebugTree())
    }
  }
}