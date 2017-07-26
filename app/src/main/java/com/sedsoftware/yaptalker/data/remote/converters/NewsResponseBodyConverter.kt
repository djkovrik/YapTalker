package com.sedsoftware.yaptalker.data.remote.converters

import com.sedsoftware.yaptalker.commons.extensions.chopEdges
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.NewsItem
import com.sedsoftware.yaptalker.data.TopicItem
import com.sedsoftware.yaptalker.data.UserProfileShort
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class NewsResponseBodyConverter : Converter<ResponseBody, List<NewsItem>> {

  companion object {
    val NEWS_SELECTOR = "td.newshead"
    val NEWS_HEADER_SELECTOR = "div.rating-short-value > a"
    val NEWS_TITLE_SELECTOR = "a.subtitle"
    val LINK_BY_ATTRIBUTE_SELECTOR = "href"
    val CONTENT_SELECTOR = "td.news-content"
    val TOPIC_INFO_SELECTOR = "td.holder"
    val TOPIC_DATE_SELECTOR = "b.icon-date"
    val PROFILE_INFO_SELECTOR = "b > a"
    val COMMENTS_COUNT_SELECTOR = "span"
  }

  override fun convert(value: ResponseBody): List<NewsItem> {

    val html = value.string()

    val htmlDocument = Jsoup.parse(html)

    val news = htmlDocument.select(NEWS_SELECTOR)
    val content = htmlDocument.select(CONTENT_SELECTOR)
    val topics = htmlDocument.select(TOPIC_INFO_SELECTOR)

    val newsCount = news.size

    assert(newsCount == content.size)
    assert(newsCount == topics.size)

    val result = ArrayList<NewsItem>(newsCount)

    // Iterate through all elements and create NewsItem objects

    for (index in 0..newsCount - 1) {

      // Parse header block

      val newsItem = news[index]
      val title = newsItem.select(NEWS_TITLE_SELECTOR).first().text()
      val header = newsItem.select(NEWS_HEADER_SELECTOR).first()

      // Skip advertisement blocks (when header == null)
      val rating = header?.text() ?: continue

      val topicLink = header.attr(LINK_BY_ATTRIBUTE_SELECTOR)
      val topicId = topicLink.getLastDigits()

      // Parse content block
      val contentItem = content[index]
      val contentBody = contentItem.html()

      // Parse topic info block
      val topicInfo = topics[index]
      val topicDate = topicInfo.select(TOPIC_DATE_SELECTOR).first().text()
      val profileInfo = topicInfo.select(PROFILE_INFO_SELECTOR)
      val nickname = profileInfo[0].text()
      val profileLink = profileInfo[0].attr(LINK_BY_ATTRIBUTE_SELECTOR)
      val userId = profileLink.getLastDigits()
      val comments = topicInfo.select(COMMENTS_COUNT_SELECTOR).text()

      // Build NewsItem
      val userInfo = UserProfileShort(id = userId, name = nickname)

      val topicItem = TopicItem(
          id = topicId,
          title = title,
          answers = Integer.parseInt(comments.chopEdges()),
          uq = Integer.parseInt(rating),
          views = 0,
          author = userInfo,
          date = topicDate)

      val singleNewsItem = NewsItem(summary = contentBody, topic = topicItem)

      result.add(singleNewsItem)
    }

    return result
  }
}
