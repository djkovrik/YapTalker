package com.sedsoftware.yaptalker.common

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber


class CrashReportingTree : Timber.Tree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    t?.let {
      when (priority) {
        Log.ERROR -> {
          Crashlytics.log(priority, tag, message)
          Crashlytics.logException(t)
        }
      }
    }
  }
}
