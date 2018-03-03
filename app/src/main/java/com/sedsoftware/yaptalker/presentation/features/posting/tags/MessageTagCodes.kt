package com.sedsoftware.yaptalker.presentation.features.posting.tags

import android.support.annotation.IntDef

class MessageTagCodes {
  companion object {
    const val TAG_B = 0
    const val TAG_I = 1
    const val TAG_U = 2
    const val TAG_LINK = 3
    const val TAG_VIDEO = 4
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(
    TAG_B,
    TAG_I,
    TAG_U,
    TAG_LINK,
    TAG_VIDEO
  )
  annotation class Tag
}
