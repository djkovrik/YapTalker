package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

data class NewsItem(
    val summary: String,
    val forum: String,
    val topic: TopicItemList) : Parcelable {

  constructor(source: Parcel) : this(
      source.readString(),
      source.readString(),
      source.readParcelable<TopicItemList>(TopicItemList::class.java.classLoader)
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(summary)
    writeString(forum)
    writeParcelable(topic, 0)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<NewsItem> = object : Parcelable.Creator<NewsItem> {
      override fun createFromParcel(source: Parcel): NewsItem = NewsItem(source)
      override fun newArray(size: Int): Array<NewsItem?> = arrayOfNulls(size)
    }
  }
}

data class NewsItemContent(val sourceHtml: String) {

  companion object {
    private val IMAGE_SELECTOR = "img[src]"
    private val VIDEO_SELECTOR = "iframe[src]"
    private val SRC_SELECTOR = "src"
  }

  var image = ""
  var video = ""

  var text: String = Jsoup
      .clean(sourceHtml, Whitelist().addTags("i", "u", "b", "br"))

  init {
    if (text.contains("<br>")) {
      text = text.substring(0, text.indexOf("<br>"))
    }

    val content = Jsoup.parse(sourceHtml)
    val imageLink = content.select(IMAGE_SELECTOR)
    val videoLink = content.select(VIDEO_SELECTOR)

    image = imageLink.attr(SRC_SELECTOR)
    video = videoLink.attr(SRC_SELECTOR)
  }
}