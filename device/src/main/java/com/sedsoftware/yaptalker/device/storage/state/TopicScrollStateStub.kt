package com.sedsoftware.yaptalker.device.storage.state

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class TopicScrollStateStub() : Parcelable {
    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<TopicScrollStateStub> {
        override fun createFromParcel(parcel: Parcel): TopicScrollStateStub {
            return TopicScrollStateStub(parcel)
        }

        override fun newArray(size: Int): Array<TopicScrollStateStub?> {
            return arrayOfNulls(size)
        }
    }
}
