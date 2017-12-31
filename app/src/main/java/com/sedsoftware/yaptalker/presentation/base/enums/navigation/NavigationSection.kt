package com.sedsoftware.yaptalker.presentation.base.enums.navigation

import android.support.annotation.IntDef

class NavigationSection {
  companion object {
    const val MAIN_PAGE = 0L
    const val FORUMS = 1L
    const val ACTIVE_TOPICS = 2L
    const val INCUBATOR = 3L
    const val BOOKMARKS = 4L
    const val SETTINGS = 5L
    const val SIGN_IN = 6L
    const val SIGN_OUT = 7L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(MAIN_PAGE, FORUMS, ACTIVE_TOPICS, INCUBATOR, BOOKMARKS, SETTINGS, SIGN_IN, SIGN_OUT)
  annotation class Section
}
