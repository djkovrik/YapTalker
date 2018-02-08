package com.sedsoftware.yaptalker.presentation.features.search.options

import android.support.annotation.IntDef
import android.support.annotation.StringDef

class TargetPeriod {
  companion object {
    const val ALL_TIME = 0L
    const val TODAY = 1L
    const val DAYS_7 = 7L
    const val DAYS_30 = 30L
    const val DAYS_60 = 60L
    const val DAYS_90 = 90L
    const val DAYS_180 = 180L
    const val DAYS_365 = 365L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(ALL_TIME, TODAY, DAYS_7, DAYS_30, DAYS_60, DAYS_90, DAYS_180, DAYS_365)
  annotation class Value
}
