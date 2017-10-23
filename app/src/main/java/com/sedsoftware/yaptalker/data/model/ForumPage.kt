package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable
import pl.droidsonroids.jspoon.annotation.Selector

class ForumPage() : Parcelable {
  @Selector("a[href~=.*/forum\\d+/].title", defValue = "Unknown")
  lateinit var forumTitle: String
  @Selector("a[href~=.*/forum\\d+/].title", attr = "href", defValue = "0")
  lateinit var forumId: String
  @Selector("td[nowrap=nowrap]", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector("td[nowrap=nowrap]", format = "(\\d+)", defValue = "0")
  lateinit var totalPages: String
  @Selector("table tr:has(td.row4)")
  lateinit var topics: List<Topic>

  constructor(parcel: Parcel) : this() {
    forumTitle = parcel.readString()
    forumId = parcel.readString()
    totalPages = parcel.readString()
    topics = parcel.createTypedArrayList(Topic)
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(forumTitle)
    parcel.writeString(forumId)
    parcel.writeString(totalPages)
    parcel.writeTypedList(topics)
  }

  override fun describeContents() = 0

  companion object CREATOR : Parcelable.Creator<ForumPage> {
    override fun createFromParcel(parcel: Parcel): ForumPage = ForumPage(parcel)
    override fun newArray(size: Int): Array<ForumPage?> = arrayOfNulls(size)
  }
}

class Topic() : Parcelable {
  @Selector("a.subtitle", defValue = "Unknown") lateinit var title: String
  @Selector("a.subtitle", attr = "href") lateinit var link: String
  @Selector("img[src*=pinned]", attr = "src", defValue = "") lateinit var isPinned: String
  @Selector("td[class~=row(2|4)] > a", defValue = "Unknown") lateinit var author: String
  @Selector("td[class~=row(2|4)] > a", attr = "href") lateinit var authorLink: String
  @Selector("div.rating-short-value", defValue = "0") lateinit var rating: String
  @Selector("td.row4:matchesOwn(\\d+)", defValue = "0") lateinit var answers: String
  @Selector("span.desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown") lateinit var lastPostDate: String
  @Selector("span.desc a ~ a", defValue = "Unknown") lateinit var lastPostAuthor: String

  constructor(parcel: Parcel) : this() {
    title = parcel.readString()
    link = parcel.readString()
    isPinned = parcel.readString()
    author = parcel.readString()
    authorLink = parcel.readString()
    rating = parcel.readString()
    answers = parcel.readString()
    lastPostDate = parcel.readString()
    lastPostAuthor = parcel.readString()
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(title)
    parcel.writeString(link)
    parcel.writeString(isPinned)
    parcel.writeString(author)
    parcel.writeString(authorLink)
    parcel.writeString(rating)
    parcel.writeString(answers)
    parcel.writeString(lastPostDate)
    parcel.writeString(lastPostAuthor)
  }

  override fun describeContents() = 0

  companion object CREATOR : Parcelable.Creator<Topic> {
    override fun createFromParcel(parcel: Parcel): Topic = Topic(parcel)
    override fun newArray(size: Int): Array<Topic?> = arrayOfNulls(size)
  }
}
