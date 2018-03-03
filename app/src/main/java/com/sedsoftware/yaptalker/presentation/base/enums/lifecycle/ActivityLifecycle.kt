package com.sedsoftware.yaptalker.presentation.base.enums.lifecycle

import android.support.annotation.IntDef

class ActivityLifecycle {

  companion object {
    const val CREATE = 0
    const val START = 1
    const val RESUME = 2
    const val PAUSE = 3
    const val STOP = 4
    const val DESTROY = 5
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(CREATE, START, RESUME, PAUSE, STOP, DESTROY)
  annotation class Event
}
