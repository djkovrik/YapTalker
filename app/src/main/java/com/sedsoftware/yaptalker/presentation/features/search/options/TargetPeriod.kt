package com.sedsoftware.yaptalker.presentation.features.search.options

import android.support.annotation.IntDef

class TargetPeriod {
  companion object {
    const val ALL_TIME = 0
    const val TODAY = 1
    const val DAYS_7 = 7
    const val DAYS_30 = 30
    const val DAYS_60 = 60
    const val DAYS_90 = 90
    const val DAYS_180 = 180
    const val DAYS_365 = 365
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(ALL_TIME, TODAY, DAYS_7, DAYS_30, DAYS_60, DAYS_90, DAYS_180, DAYS_365)
  annotation class Value
}
