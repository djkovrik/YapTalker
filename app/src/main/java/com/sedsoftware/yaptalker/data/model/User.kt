package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable

data class UserProfileShort(
    val id: Int,
    val name: String) : Parcelable {

  constructor(source: Parcel) : this(
      source.readInt(),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeInt(id)
    writeString(name)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<UserProfileShort> = object : Parcelable.Creator<UserProfileShort> {
      override fun createFromParcel(source: Parcel): UserProfileShort = UserProfileShort(source)
      override fun newArray(size: Int): Array<UserProfileShort?> = arrayOfNulls(size)
    }
  }
}

data class UserProfile(
    val id: Int,
    val name: String,
    val avatar: String,
    val registered: String) : Parcelable {

  constructor(source: Parcel) : this(
      source.readInt(),
      source.readString(),
      source.readString(),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeInt(id)
    writeString(name)
    writeString(avatar)
    writeString(registered)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<UserProfile> = object : Parcelable.Creator<UserProfile> {
      override fun createFromParcel(source: Parcel): UserProfile = UserProfile(source)
      override fun newArray(size: Int): Array<UserProfile?> = arrayOfNulls(size)
    }
  }
}
