package com.sedsoftware.yaptalker.presentation.base.enums

import android.support.annotation.LongDef

class ConnectionState {

  companion object {
    const val IDLE = 0L
    const val LOADING = 1L
    const val COMPLETED = 2L
    const val ERROR = 3L
  }

  @Retention(AnnotationRetention.SOURCE)
  @LongDef(IDLE, LOADING, COMPLETED, ERROR)
  annotation class Event
}
