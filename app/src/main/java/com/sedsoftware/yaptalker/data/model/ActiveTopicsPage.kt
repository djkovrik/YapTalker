package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import pl.droidsonroids.jspoon.annotation.Selector

class ActiveTopicsPage() : Parcelable {
  @Selector("form[name=dateline]")
  lateinit var navigation: ActiveTopicsNavigationPanel
  @Selector("table tr:has(td.row4)")
  lateinit var topics: List<ActiveTopic>

  constructor(parcel: Parcel) : this() {
    navigation = parcel.readParcelable(ActiveTopicsNavigationPanel::class.java.classLoader)
    topics = parcel.createTypedArrayList(ActiveTopic)
  }

  constructor(navigation: ActiveTopicsNavigationPanel, topics: List<ActiveTopic>) : this() {
    this.navigation = navigation
    this.topics = topics
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeParcelable(navigation, flags)
    parcel.writeTypedList(topics)
  }

  override fun describeContents() = 0

  companion object CREATOR : Creator<ActiveTopicsPage> {
    override fun createFromParcel(parcel: Parcel): ActiveTopicsPage {
      return ActiveTopicsPage(parcel)
    }

    override fun newArray(size: Int): Array<ActiveTopicsPage?> = arrayOfNulls(size)
  }
}

class ActiveTopicsNavigationPanel() : Parcelable, ViewType {
  @Selector(".pagelinks", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector(".pagelinks", format = "(\\d+)", defValue = "0")
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

  companion object CREATOR : Creator<ActiveTopicsNavigationPanel> {
    override fun createFromParcel(parcel: Parcel): ActiveTopicsNavigationPanel {
      return ActiveTopicsNavigationPanel(parcel)
    }

    override fun newArray(size: Int): Array<ActiveTopicsNavigationPanel?> = arrayOfNulls(size)
  }

  override fun getViewType() = ContentTypes.NAVIGATION_PANEL
}

class ActiveTopic() : Parcelable, ViewType {
  @Selector("a.subtitle", defValue = "Unknown") lateinit var title: String
  @Selector("a.subtitle", attr = "href") lateinit var link: String
  @Selector("img[src*=pinned]", attr = "src", defValue = "") lateinit var isPinned: String
  @Selector("img[src*=closed]", attr = "src", defValue = "") lateinit var isClosed: String
  @Selector("td[class~=row(2|4)] > a", defValue = "Unknown") lateinit var forumTitle: String
  @Selector("td[class~=row(2|4)] > a", attr = "href") lateinit var forumLink: String
  @Selector("div.rating-short-value", defValue = "0") lateinit var rating: String
  @Selector("td.row4:matchesOwn(\\d+)", defValue = "0") lateinit var answers: String
  @Selector("span.desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown") lateinit var lastPostDate: String

  constructor(parcel: Parcel) : this() {
    title = parcel.readString()
    link = parcel.readString()
    isPinned = parcel.readString()
    isClosed = parcel.readString()
    forumTitle = parcel.readString()
    forumLink = parcel.readString()
    rating = parcel.readString()
    answers = parcel.readString()
    lastPostDate = parcel.readString()
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(title)
    parcel.writeString(link)
    parcel.writeString(isPinned)
    parcel.writeString(isClosed)
    parcel.writeString(forumTitle)
    parcel.writeString(forumLink)
    parcel.writeString(rating)
    parcel.writeString(answers)
    parcel.writeString(lastPostDate)
  }

  override fun describeContents() = 0

  companion object CREATOR : Creator<ActiveTopic> {
    override fun createFromParcel(parcel: Parcel): ActiveTopic {
      return ActiveTopic(parcel)
    }

    override fun newArray(size: Int): Array<ActiveTopic?> = arrayOfNulls(size)
  }

  override fun getViewType() = ContentTypes.ACTIVE_TOPIC
}
