package com.sedsoftware.yaptalker.base.navigation

import android.support.annotation.IntDef

class NavigationDrawerItems {
  companion object {
    const val MAIN_PAGE = 0L
    const val FORUMS = 1L
    const val ACTIVE_TOPICS = 2L
    const val BOOKMARKS = 3L
    const val SETTINGS = 4L
    const val SIGN_IN = 5L
    const val SIGN_OUT = 6L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(MAIN_PAGE, FORUMS, ACTIVE_TOPICS, BOOKMARKS, SETTINGS, SIGN_IN, SIGN_OUT)
  annotation class Section
}
