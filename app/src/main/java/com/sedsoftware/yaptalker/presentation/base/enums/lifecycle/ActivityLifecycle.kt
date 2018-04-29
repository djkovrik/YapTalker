package com.sedsoftware.yaptalker.presentation.base.enums.lifecycle

import android.support.annotation.LongDef

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
  @LongDef(CREATE, START, RESUME, PAUSE, STOP, DESTROY)
  annotation class Event
}
