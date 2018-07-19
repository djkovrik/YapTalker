package com.sedsoftware.yaptalker.device.storage.state

import android.os.Parcelable

class TopicState(val scrollState: Parcelable, val currentPage: Int, val totalPages: Int, val label: String)
