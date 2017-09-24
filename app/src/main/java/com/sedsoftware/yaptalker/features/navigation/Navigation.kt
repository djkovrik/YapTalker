package com.sedsoftware.yaptalker.features.navigation

import android.support.annotation.IntDef

class Navigation {
  companion object {
    const val MAIN_PAGE = 0L
    const val FORUMS = 1L
    const val SETTINGS = 2L
    const val AUTHORIZATION = 3L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(MAIN_PAGE, FORUMS, SETTINGS, AUTHORIZATION)
  annotation class Section
}
