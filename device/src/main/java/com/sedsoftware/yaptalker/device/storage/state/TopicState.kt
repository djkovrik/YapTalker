package com.sedsoftware.yaptalker.device.storage.state

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.support.v7.widget.LinearLayoutManager.SavedState

data class TopicState(
    val forumId: Int = 0,
    val topicId: Int = 0,
    val currentPage: Int = 0,
    val scrollState: SavedState? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(SavedState::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(forumId)
        parcel.writeInt(topicId)
        parcel.writeInt(currentPage)
        parcel.writeParcelable(scrollState, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Creator<TopicState> {
        override fun createFromParcel(parcel: Parcel): TopicState =
            TopicState(parcel)

        override fun newArray(size: Int): Array<TopicState?> =
            arrayOfNulls(size)
    }
}
