package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable
import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import pl.droidsonroids.jspoon.annotation.Selector

class Forums {
  @Selector("td.row4 > b > a.title") lateinit var titles: List<String>
  @Selector("td.row4 > b > a.title", attr = "href") lateinit var ids: List<String>
  @Selector("td.row2[nowrap=nowrap]") lateinit var topics: List<LastTopic>
}

class LastTopic {
  @Selector("a.subtitle", defValue = "Unknown") lateinit var title: String
  @Selector("a[href~=members]", defValue = "Unknown") lateinit var author: String
  @Selector(".desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown") lateinit var date: String
}

fun Forums.createForumsList(): List<ForumItem> {

  check(titles.size == ids.size) { "Titles size should match ids size" }
  check(topics.size == ids.size) { "Topics size should match ids size" }

  val result: MutableList<ForumItem> = ArrayList()

  titles.forEachIndexed { index, _ ->
    result.add(ForumItem(
        title = titles[index],
        forumId = ids[index].getLastDigits(),
        lastTopicTitle = topics[index].title,
        lastTopicAuthor = topics[index].author,
        date = topics[index].date))
  }

  return result
}

data class ForumItem(
    val title: String,
    val forumId: Int,
    val lastTopicTitle: String,
    val lastTopicAuthor: String,
    val date: String) : ViewType, Parcelable {

  constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readInt(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(title)
    parcel.writeInt(forumId)
    parcel.writeString(lastTopicTitle)
    parcel.writeString(lastTopicAuthor)
    parcel.writeString(date)
  }

  override fun describeContents() = 0

  companion object CREATOR : Parcelable.Creator<ForumItem> {
    override fun createFromParcel(parcel: Parcel): ForumItem {
      return ForumItem(parcel)
    }

    override fun newArray(size: Int): Array<ForumItem?> = arrayOfNulls(size)
  }

  override fun getViewType() = ContentTypes.FORUM
}
