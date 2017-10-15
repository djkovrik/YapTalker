package com.sedsoftware.yaptalker.base.events

import android.support.annotation.IntDef

class ActivityLifecycle {

  companion object {
    const val CREATE = 0L
    const val START = 1L
    const val RESUME = 2L
    const val PAUSE = 3L
    const val STOP = 4L
    const val DESTROY = 5L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(CREATE, START, RESUME, PAUSE, STOP, DESTROY)
  annotation class Event
}
