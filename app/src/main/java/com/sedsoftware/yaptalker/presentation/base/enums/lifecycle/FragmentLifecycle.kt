package com.sedsoftware.yaptalker.presentation.base.enums.lifecycle

import android.support.annotation.LongDef

class FragmentLifecycle {

  companion object {
    const val ATTACH = 0L
    const val CREATE = 1L
    const val START = 2L
    const val RESUME = 3L
    const val PAUSE = 4L
    const val STOP = 5L
    const val DESTROY = 6L
    const val DETACH = 7L
  }

  @Retention(AnnotationRetention.SOURCE)
  @LongDef(ATTACH, CREATE, START, RESUME, PAUSE, STOP, DESTROY, DETACH)
  annotation class Event
}
