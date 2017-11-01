package com.sedsoftware.yaptalker.data.parsing

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import pl.droidsonroids.jspoon.annotation.Selector

class ForumPage() : Parcelable {
  @Selector("a[href~=.*/forum\\d+/].title", defValue = "Unknown")
  lateinit var forumTitle: String
  @Selector("a[href~=.*/forum\\d+/].title", attr = "href", defValue = "0")
  lateinit var forumId: String
  @Selector("table[width=100%]")
  lateinit var navigation: ForumNavigationPanel
  @Selector("table tr:has(td.row4)")
  lateinit var topics: List<Topic>

  constructor(parcel: Parcel) : this() {
    forumTitle = parcel.readString()
    forumId = parcel.readString()
    navigation = parcel.readParcelable(ForumNavigationPanel::class.java.classLoader)
    topics = parcel.createTypedArrayList(Topic)
  }

  constructor(title: String, id: String, navigationPanel: ForumNavigationPanel, topicsList: List<Topic>) : this() {
    forumTitle = title
    forumId = id
    navigation = navigationPanel
    topics = topicsList
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(forumTitle)
    parcel.writeString(forumId)
    parcel.writeParcelable(navigation, flags)
    parcel.writeTypedList(topics)
  }

  override fun describeContents() = 0

  companion object CREATOR : Creator<ForumPage> {
    override fun createFromParcel(parcel: Parcel): ForumPage {
      return ForumPage(parcel)
    }

    override fun newArray(size: Int): Array<ForumPage?> = arrayOfNulls(size)
  }
}

class ForumNavigationPanel() : ViewType, Parcelable {
  @Selector("td[nowrap=nowrap]", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector("td[nowrap=nowrap]", format = "(\\d+)", defValue = "0")
  lateinit var totalPages: String

  constructor(parcel: Parcel) : this() {
    currentPage = parcel.readString()
    totalPages = parcel.readString()
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(currentPage)
    parcel.writeString(totalPages)
  }

  override fun describeContents() = 0

  companion object CREATOR : Creator<ForumNavigationPanel> {
    override fun createFromParcel(parcel: Parcel): ForumNavigationPanel {
      return ForumNavigationPanel(parcel)
    }

    override fun newArray(size: Int): Array<ForumNavigationPanel?> = arrayOfNulls(size)
  }

  override fun getViewType() = ContentTypes.NAVIGATION_PANEL
}

class Topic() : ViewType, Parcelable {
  @Selector("a.subtitle", defValue = "Unknown") lateinit var title: String
  @Selector("a.subtitle", attr = "href") lateinit var link: String
  @Selector("img[src*=pinned]", attr = "src", defValue = "") lateinit var isPinned: String
  @Selector("img[src*=closed]", attr = "src", defValue = "") lateinit var isClosed: String
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

  companion object CREATOR : Creator<Topic> {
    override fun createFromParcel(parcel: Parcel): Topic {
      return Topic(parcel)
    }

    override fun newArray(size: Int): Array<Topic?> = arrayOfNulls(size)
  }

  override fun getViewType() = ContentTypes.TOPIC
}
