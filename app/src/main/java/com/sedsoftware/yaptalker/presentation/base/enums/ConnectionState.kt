package com.sedsoftware.yaptalker.presentation.base.enums

import android.support.annotation.IntDef

class ConnectionState {

  companion object {
    const val IDLE = 0
    const val LOADING = 1
    const val COMPLETED = 2
    const val ERROR = 3
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(IDLE, LOADING, COMPLETED, ERROR)
  annotation class Event
}
