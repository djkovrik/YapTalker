package com.sedsoftware.yaptalker.presentation.base.enums

import android.support.annotation.IntDef

class ConnectionState {

  companion object {
    const val IDLE = 0L
    const val LOADING = 1L
    const val COMPLETED = 2L
    const val ERROR = 3L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(IDLE, LOADING, COMPLETED, ERROR)
  annotation class Event
}
