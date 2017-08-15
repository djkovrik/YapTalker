package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable
import com.sedsoftware.yaptalker.commons.extensions.extractDate
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import pl.droidsonroids.jspoon.annotation.Selector

class Forums {
  @Selector("td.row4 > b > a.title") lateinit var titles: List<String>
  @Selector("td.row4 > b > a.title", attr = "href") lateinit var ids: List<String>
  @Selector("td.row2[nowrap=nowrap]") lateinit var topics: List<LastTopic>
}

class LastTopic {
  @Selector(".desc", attr = "innerHtml") lateinit var htmlDesc: String
  @Selector("a.subtitle") lateinit var title: String
  @Selector("a ~ a ~ a") lateinit var author: String
}

fun Forums.createForumsList(): List<ForumItem> {

  assert(titles.size == ids.size, { "Titles size should match ids size" })
  assert(topics.size == ids.size, { "Topics size should match ids size" })

  val result: MutableList<ForumItem> = ArrayList()

  titles.forEachIndexed { index, _ ->
    result.add(ForumItem(
        title = titles[index],
        forumId = ids[index].getLastDigits(),
        lastTopicTitle = topics[index].title,
        lastTopicAuthor = topics[index].author,
        htmlDesc = topics[index].htmlDesc))
  }

  return result
}

data class ForumItem(
    val title: String,
    val forumId: Int,
    val lastTopicTitle: String,
    val lastTopicAuthor: String,
    val htmlDesc: String) : Parcelable {

  val date
    get() = htmlDesc.extractDate()

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
    parcel.writeString(htmlDesc)
  }

  override fun describeContents() = 0

  companion object CREATOR : Parcelable.Creator<ForumItem> {
    override fun createFromParcel(parcel: Parcel): ForumItem {
      return ForumItem(parcel)
    }

    override fun newArray(size: Int): Array<ForumItem?> = arrayOfNulls(size)
  }
}
