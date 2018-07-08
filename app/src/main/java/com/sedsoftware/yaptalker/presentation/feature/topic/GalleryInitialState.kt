package com.sedsoftware.yaptalker.presentation.feature.topic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryInitialState(
    val currentForumId: Int,
    val currentTopicId: Int,
    val currentPage: Int = 1,
    val currentImage: String = ""
) : Parcelable
