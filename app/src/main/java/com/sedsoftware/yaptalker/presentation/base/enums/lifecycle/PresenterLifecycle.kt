package com.sedsoftware.yaptalker.presentation.base.enums.lifecycle

import android.support.annotation.IntDef

class PresenterLifecycle {

  companion object {
    const val CREATE = 0L
    const val ATTACH_VIEW = 1L
    const val DETACH_VIEW = 2L
    const val DESTROY = 3L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(CREATE, DESTROY)
  annotation class Event
}
