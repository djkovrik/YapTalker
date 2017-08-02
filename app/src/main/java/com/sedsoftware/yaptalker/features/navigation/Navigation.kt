package com.sedsoftware.yaptalker.features.navigation

import android.support.annotation.IntDef

class Navigation {
  companion object {
    const val MAIN_PAGE = 0L
    const val FORUMS = 1L
    const val SETTINGS = 2L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(MAIN_PAGE, FORUMS, SETTINGS)
  annotation class Section
}