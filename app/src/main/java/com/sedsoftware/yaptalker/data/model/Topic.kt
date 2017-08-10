package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable

data class TopicItemList(
    val id: Int,
    val title: String,
    val answers: Int,
    val uq: Int,
    val author: UserProfileShort,
    val date: String) : Parcelable {

  constructor(source: Parcel) : this(
      source.readInt(),
      source.readString(),
      source.readInt(),
      source.readInt(),
      source.readParcelable<UserProfileShort>(UserProfileShort::class.java.classLoader),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeInt(id)
    writeString(title)
    writeInt(answers)
    writeInt(uq)
    writeParcelable(author, 0)
    writeString(date)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<TopicItemList> = object : Parcelable.Creator<TopicItemList> {
      override fun createFromParcel(source: Parcel): TopicItemList = TopicItemList(source)
      override fun newArray(size: Int): Array<TopicItemList?> = arrayOfNulls(size)
    }
  }
}

data class TopicItemShort(
    val id: Int,
    val title: String,
    val author: UserProfileShort,
    val date: String) : Parcelable {

  constructor(source: Parcel) : this(
      source.readInt(),
      source.readString(),
      source.readParcelable<UserProfileShort>(UserProfileShort::class.java.classLoader),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeInt(id)
    writeString(title)
    writeParcelable(author, 0)
    writeString(date)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<TopicItemShort> = object : Parcelable.Creator<TopicItemShort> {
      override fun createFromParcel(source: Parcel): TopicItemShort = TopicItemShort(source)
      override fun newArray(size: Int): Array<TopicItemShort?> = arrayOfNulls(size)
    }
  }
}
