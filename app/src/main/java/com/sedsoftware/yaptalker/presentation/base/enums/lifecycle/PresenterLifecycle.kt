package com.sedsoftware.yaptalker.presentation.base.enums.lifecycle

import android.support.annotation.IntDef

class PresenterLifecycle {

  companion object {
    const val CREATE = 0
    const val ATTACH_VIEW = 1
    const val DETACH_VIEW = 2
    const val DESTROY = 3
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(CREATE, DESTROY)
  annotation class Event
}
