package com.sedsoftware.yaptalker.data.parsing

import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
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
  @Selector(".subtitle", defValue = "Unknown") lateinit var title: String
  @Selector(".subtitle", attr = "href", defValue = "") lateinit var link: String
  @Selector(".rating-short-value > a", defValue = "") lateinit var rating: String
}

class NewsContent {
  @Selector("[id~=news_.*]", attr = "innerHtml", defValue = "") lateinit var description: String
  @Selector("img[src]", attr = "src") lateinit var images: List<String>
  @Selector("iframe[src]", attr = "src") lateinit var videos: List<String>
  @Selector("iframe[src]", attr = "outerHtml") lateinit var videosRaw: List<String>
}

class NewsBottom {
  @Selector(".icon-user > a", defValue = "Unknown") lateinit var author: String
  @Selector(".icon-user > a", attr = "href") lateinit var authorLink: String
  @Selector(".icon-date", defValue = "Unknown") lateinit var date: String
  @Selector(".icon-forum > a", defValue = "Unknown") lateinit var forumName: String
  @Selector(".icon-forum > a", attr = "href", defValue = "") lateinit var forumLink: String
  @Selector("span", defValue = "0") lateinit var comments: String
}

fun News.createNewsList(): List<NewsItem> {

  check(headers.size == contents.size) { "Headers size should match contents size" }
  check(contents.size == bottoms.size) { "Contents size should match bottoms size" }

  val result: MutableList<NewsItem> = ArrayList()

  headers.forEachIndexed { index, _ ->

    result.add(NewsItem(
        title = headers[index].title,
        link = headers[index].link,
        rating = headers[index].rating,
        description = contents[index].description,
        images = contents[index].images,
        videos = contents[index].videos,
        videosRaw = contents[index].videosRaw,
        author = bottoms[index].author,
        authorLink = bottoms[index].authorLink,
        date = bottoms[index].date,
        forumName = bottoms[index].forumName,
        forumLink = bottoms[index].forumLink,
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
    val videosRaw: List<String>,
    val author: String,
    val authorLink: String,
    val date: String,
    val forumName: String,
    val forumLink: String,
    val comments: String) : ViewType {

  val cleanedDescription: String =
      with(Jsoup.clean(description, Whitelist().addTags("i", "u", "b", "br"))) {
        if (this.contains("<br>"))
          this.substring(0, this.indexOf("<br>"))
        else
          this
      }

  override fun getViewType() = ContentTypes.NEWS
}
