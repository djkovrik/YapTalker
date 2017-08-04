package com.sedsoftware.yaptalker.data.remote

import android.support.annotation.IntDef

class YapRequestState {

  companion object {
    const val IDLE = 0L
    const val LOADING = 1L
    const val COMPLETED = 2L
    const val ERROR = 3L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(IDLE, LOADING, COMPLETED, ERROR)
  annotation class State
}