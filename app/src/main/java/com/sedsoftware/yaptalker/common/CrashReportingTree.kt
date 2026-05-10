package com.sedsoftware.yaptalker.common

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber


class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        t?.let {
            when (priority) {
                Log.ERROR -> {
                    FirebaseCrashlytics.getInstance().log("${tag.orEmpty()}: $message")
                    FirebaseCrashlytics.getInstance().recordException(t)
                }
            }
        }
    }
}
