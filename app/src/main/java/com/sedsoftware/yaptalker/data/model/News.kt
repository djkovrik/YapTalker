package com.sedsoftware.yaptalker.data.model

import android.os.Parcel
import android.os.Parcelable
import com.sedsoftware.yaptalker.commons.extensions.chopEdges
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import pl.droidsonroids.jspoon.annotation.Selector

class News {
  @Selector(".newshead") lateinit var headers: List<NewsHead>
  @Selector(".news-content") lateinit var contents: List<NewsContent>
  @Selector(".newsbottom") lateinit var bottoms: List<NewsBottom>
}

class NewsHead {
  @Selector(".subtitle") lateinit var title: String
  @Selector(".subtitle", attr = "href") lateinit var link: String
  @Selector(".rating-short-value > a", defValue = "") lateinit var rating: String
}

class NewsContent {
  @Selector("[id~=news_.*]", attr = "innerHtml") lateinit var description: String
  @Selector("img[src]", attr = "src") lateinit var images: List<String>
  @Selector("iframe[src]", attr = "src") lateinit var videos: List<String>
}

class NewsBottom {
  @Selector(".icon-user > a") lateinit var author: String
  @Selector(".icon-user > a", attr = "href") lateinit var authorLink: String
  @Selector(".icon-date") lateinit var date: String
  @Selector(".icon-forum > a") lateinit var forumName: String
  @Selector("span") lateinit var comments: String
}

fun News.createNewsList(): List<NewsItem> {

  assert(headers.size == contents.size, { "Headers size should match contents size" })
  assert(contents.size == bottoms.size, { "Contents size should match bottoms size" })

  val result: MutableList<NewsItem> = ArrayList()

  headers.forEachIndexed { index, _ ->

    result.add(NewsItem(
        title = headers[index].title,
        link = headers[index].link,
        rating = headers[index].rating,
        description = contents[index].description,
        images = contents[index].images,
        videos = contents[index].videos,
        author = bottoms[index].author,
        authorLink = bottoms[index].authorLink,
        date = bottoms[index].date,
        forumName = bottoms[index].forumName,
        comments = bottoms[index].comments.chopEdges()))
  }

  return result
}

data class NewsItem(
    val title: String,
    val link: String,
    val rating: String,
    val description: String,
    val images: List<String>,
    val videos: List<String>,
    val author: String,
    val authorLink: String,
    val date: String,
    val forumName: String,
    val comments: String) : Parcelable {

  val cleanedDescription: String
    get() {
      with(Jsoup.clean(description, Whitelist().addTags("i", "u", "b", "br"))) {
        if (this.contains("<br>"))
          return this.substring(0, this.indexOf("<br>"))
        else
          return this
      }
    }

  constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.createStringArrayList(),
      parcel.createStringArrayList(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(title)
    parcel.writeString(link)
    parcel.writeString(rating)
    parcel.writeString(description)
    parcel.writeStringList(images)
    parcel.writeStringList(videos)
    parcel.writeString(author)
    parcel.writeString(authorLink)
    parcel.writeString(date)
    parcel.writeString(forumName)
    parcel.writeString(comments)
  }

  override fun describeContents() = 0

  companion object CREATOR : Parcelable.Creator<NewsItem> {
    override fun createFromParcel(parcel: Parcel): NewsItem {
      return NewsItem(parcel)
    }

    override fun newArray(size: Int): Array<NewsItem?> = arrayOfNulls(size)
  }
}
