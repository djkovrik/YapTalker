package com.sedsoftware.yaptalker.features.base

import android.support.annotation.IntDef

class BasePresenterLifecycle {

  companion object {
    const val CREATE = 0L
    const val DESTROY = 1L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(
      CREATE,
      DESTROY)
  annotation class Event
}
