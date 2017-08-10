package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable

data class PostItem(
    val id: Int,
    val author: UserProfile,
    val date: String,
    val uq: Int,
    val content: String) : Parcelable {

  constructor(source: Parcel) : this(
      source.readInt(),
      source.readParcelable<UserProfile>(UserProfile::class.java.classLoader),
      source.readString(),
      source.readInt(),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeInt(id)
    writeParcelable(author, 0)
    writeString(date)
    writeInt(uq)
    writeString(content)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<PostItem> = object : Parcelable.Creator<PostItem> {
      override fun createFromParcel(source: Parcel): PostItem = PostItem(source)
      override fun newArray(size: Int): Array<PostItem?> = arrayOfNulls(size)
    }
  }
}