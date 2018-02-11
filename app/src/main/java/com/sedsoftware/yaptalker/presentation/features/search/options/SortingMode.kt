package com.sedsoftware.yaptalker.presentation.features.search.options

import android.support.annotation.StringDef

class SortingMode {
  companion object {
    const val RELEVANT = "rel"
    const val DATE = "date"
  }

  @Retention(AnnotationRetention.SOURCE)
  @StringDef(RELEVANT, DATE)
  annotation class Value
}
