package com.sedsoftware.yaptalker.presentation.base.enums.lifecycle

import android.support.annotation.IntDef

class FragmentLifecycle {

  companion object {
    const val ATTACH = 0
    const val CREATE = 1
    const val START = 2
    const val RESUME = 3
    const val PAUSE = 4
    const val STOP = 5
    const val DESTROY = 6
    const val DETACH = 7
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(ATTACH, CREATE, START, RESUME, PAUSE, STOP, DESTROY, DETACH)
  annotation class Event
}
