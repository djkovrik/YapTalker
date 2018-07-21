package com.sedsoftware.yaptalker.device.storage.state

import android.os.Parcelable

class TopicState(
    val forumId: Int = 0,
    val topicId: Int = 0,
    val currentPage: Int = 0,
    val scrollState: Parcelable = TopicScrollStateStub()
)
