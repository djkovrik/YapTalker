package com.sedsoftware.yaptalker.features.navigation

import android.support.annotation.IntDef

class NavigationDrawerItems {
  companion object {
    const val MAIN_PAGE = 0L
    const val FORUMS = 1L
    const val ACTIVE_TOPICS = 2L
    const val SETTINGS = 3L
    const val SIGN_IN = 4L
    const val SIGN_OUT = 5L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(MAIN_PAGE, FORUMS, ACTIVE_TOPICS, SETTINGS, SIGN_IN, SIGN_OUT)
  annotation class Section
}
