package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable
import pl.droidsonroids.jspoon.annotation.Selector

class TopicPage {
  @Selector("h1.subpage > a.subtitle", defValue = "Unknown")
  lateinit var topicTitle: String
  @Selector("td[nowrap=nowrap]:has(a[onclick~=multi_page_jump])", format = "(\\d+)", defValue = "1")
  lateinit var totalPages: String
  @Selector("table[id~=p_row_\\d+]:has(.normalname)")
  lateinit var posts: List<TopicPost>
}

class TopicPost() : Parcelable {
  @Selector(".normalname", defValue = "Unknown")
  lateinit var authorNickname: String
  @Selector("a[title=Профиль]", attr = "href", defValue = "")
  lateinit var authorProfile: String
  @Selector("a[title=Профиль] img", attr = "src", defValue = "//www.yaplakal.com/html/static/noavatar.gif")
  lateinit var authorAvatar: String
  @Selector("div[align=left][style=padding-left:5px]", format = "Сообщений: (\\d+)", defValue = "0")
  lateinit var authorMessagesCount: String
  @Selector("a.anchor", defValue = "")
  lateinit var postDate: String
  @Selector("span[class~=rank-\\w+]", defValue = "")
  lateinit var postRank: String
  @Selector("td[width*=100%][valign*=top]", attr = "innerHtml", defValue = "")
  lateinit var postContent: String
  @Selector("a[name~=entry]", attr = "outerHtml", format = "entry(\\d+)", defValue = "0")
  lateinit var postId: String

  constructor(parcel: Parcel) : this() {
    authorNickname = parcel.readString()
    authorProfile = parcel.readString()
    authorAvatar = parcel.readString()
    authorMessagesCount = parcel.readString()
    postDate = parcel.readString()
    postRank = parcel.readString()
    postContent = parcel.readString()
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(authorNickname)
    parcel.writeString(authorProfile)
    parcel.writeString(authorAvatar)
    parcel.writeString(authorMessagesCount)
    parcel.writeString(postDate)
    parcel.writeString(postRank)
    parcel.writeString(postContent)
  }

  override fun describeContents() = 0

  companion object CREATOR : Parcelable.Creator<TopicPost> {
    override fun createFromParcel(parcel: Parcel): TopicPost {
      return TopicPost(parcel)
    }

    override fun newArray(size: Int): Array<TopicPost?> = arrayOfNulls(size)
  }
}
