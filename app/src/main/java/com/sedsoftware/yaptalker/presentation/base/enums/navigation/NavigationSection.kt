package com.sedsoftware.yaptalker.presentation.base.enums.navigation

import android.support.annotation.IntDef

class NavigationSection {
  companion object {
    const val MAIN_PAGE = 0
    const val FORUMS = 1
    const val ACTIVE_TOPICS = 2
    const val INCUBATOR = 3
    const val BOOKMARKS = 4
    const val SITE_SEARCH = 5
    const val SETTINGS = 6
    const val SIGN_IN = 7
    const val SIGN_OUT = 8
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(MAIN_PAGE, FORUMS, ACTIVE_TOPICS, INCUBATOR, BOOKMARKS, SITE_SEARCH, SETTINGS, SIGN_IN, SIGN_OUT)
  annotation class Section
}
