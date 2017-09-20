package com.sedsoftware.yaptalker.commons.enums

import android.support.annotation.IntDef

class PresenterLifecycle {

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
