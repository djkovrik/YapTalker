package com.sedsoftware.yaptalker.data.remote.converters

import com.sedsoftware.yaptalker.commons.extensions.chopEdges
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.NewsItem
import com.sedsoftware.yaptalker.data.TopicItemList
import com.sedsoftware.yaptalker.data.UserProfileShort
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class NewsResponseBodyConverter : Converter<ResponseBody, List<NewsItem>> {

  companion object {
    // Selectors
    private val NEWS_SELECTOR = "td.newshead"
    private val NEWS_HEADER_SELECTOR = "div.rating-short-value > a"
    private val NEWS_TITLE_SELECTOR = "a.subtitle"
    private val LINK_BY_ATTRIBUTE_SELECTOR = "href"
    private val CONTENT_SELECTOR = "td.news-content"
    private val TOPIC_INFO_SELECTOR = "td.holder"
    private val TOPIC_DATE_SELECTOR = "b.icon-date"
    private val PROFILE_INFO_SELECTOR = "b > a"
    private val COMMENTS_COUNT_SELECTOR = "span"

    // Defaults for not parsed values
    private val STRING_DEFAULT = "Unknown news item value"
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
      val title = newsItem.select(NEWS_TITLE_SELECTOR)?.first()?.text() ?: STRING_DEFAULT
      val header = newsItem.select(NEWS_HEADER_SELECTOR)?.first()

      // Skip advertisement blocks (when header == null)
      val rating = header?.text()?.toInt() ?: continue

      val topicId = header.attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()

      // Parse content block
      val contentBody = content[index]?.html() ?: STRING_DEFAULT

      // Parse topic info block
      val topicInfo = topics[index]
      val topicDate = topicInfo.select(TOPIC_DATE_SELECTOR)?.first()?.text() ?: STRING_DEFAULT

      val comments = topicInfo.select(COMMENTS_COUNT_SELECTOR).text().chopEdges().toInt()

      val profileInfo = topicInfo.select(PROFILE_INFO_SELECTOR)
      val nickname = profileInfo[0]?.text() ?: STRING_DEFAULT

      val userId = profileInfo[0].attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()

      // Build NewsItem
      val userInfo = UserProfileShort(id = userId, name = nickname)

      val topicItem = TopicItemList(
          id = topicId,
          title = title,
          answers = comments,
          uq = rating,
          author = userInfo,
          date = topicDate)

      val singleNewsItem = NewsItem(summary = contentBody, topic = topicItem)

      result.add(singleNewsItem)
    }

    return result
  }
}
