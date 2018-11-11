package com.sedsoftware.yaptalker.presentation.feature.incubator.adapter

interface IncubatorElementsClickListener {

    fun onIncubatorItemClicked(
        forumId: Int,
        topicId: Int
    )

    fun onMediaPreviewClicked(
        url: String,
        directUrl: String = "",
        type: String = "",
        html: String = "",
        isVideo: Boolean = false
    )
}
