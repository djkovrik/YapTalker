package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable

data class ForumItem(
    val id: Int,
    val title: String,
    val lastTopic: TopicItemShort) : Parcelable {

  constructor(source: Parcel) : this(
      source.readInt(),
      source.readString(),
      source.readParcelable<TopicItemShort>(TopicItemShort::class.java.classLoader)
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeInt(id)
    writeString(title)
    writeParcelable(lastTopic, 0)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<ForumItem> = object : Parcelable.Creator<ForumItem> {
      override fun createFromParcel(source: Parcel): ForumItem = ForumItem(source)
      override fun newArray(size: Int): Array<ForumItem?> = arrayOfNulls(size)
    }
  }
}