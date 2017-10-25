package com.sedsoftware.yaptalker.features.posting

import android.support.annotation.IntDef

class MessageTags {
  companion object {
    const val TAG_B = 0L
    const val TAG_I = 1L
    const val TAG_U = 2L
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(TAG_B, TAG_I, TAG_U) annotation class Tag
}
