package com.sedsoftware.yaptalker

import android.app.Application
import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.di.DaggerApplicationComponent

class YapTalkerApp : Application() {

  companion object {
    lateinit var appComponent: ApplicationComponent
  }

  override fun onCreate() {
    super.onCreate()

    // TODO() Init Timber with Firebase Crash Reporting here

    appComponent = DaggerApplicationComponent.builder().build()
  }
}