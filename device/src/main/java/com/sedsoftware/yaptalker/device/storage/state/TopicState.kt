package com.sedsoftware.yaptalker.device.storage.state

import android.os.Parcelable

class TopicState(
    val forumId: Int,
    val topicId: Int,
    val currentPage: Int,
    val scrollState: Parcelable)
