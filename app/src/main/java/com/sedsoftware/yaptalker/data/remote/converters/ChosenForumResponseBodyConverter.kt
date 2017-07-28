package com.sedsoftware.yaptalker.data.remote.converters

import com.sedsoftware.yaptalker.commons.extensions.extractDate
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.TopicItem
import com.sedsoftware.yaptalker.data.UserProfileShort
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class ChosenForumResponseBodyConverter : Converter<ResponseBody, List<TopicItem>> {

  companion object {
    private val TOPIC_TITLES_SELECTOR = "td.row4 > div > a"
    private val TOPIC_AUTHORS_SELECTOR = "td.row2 > a"
    private val TOPIC_ANSWERS_SELECTOR = "td[align].row4 + td[align].row2 + td.row4 + td[align].row2 + td[align].row4"
    private val TOPIC_UQ_SELECTOR = "div.rating-short-value"
    private val TOPIC_DATES_SELECTOR = "td.row2 > span.desc"
    private val LINK_BY_ATTRIBUTE_SELECTOR = "href"
  }

  override fun convert(value: ResponseBody): List<TopicItem> {

    val html = value.string()
    val htmlDocument = Jsoup.parse(html)

    val titles = htmlDocument.select(TOPIC_TITLES_SELECTOR)
    val authors = htmlDocument.select(TOPIC_AUTHORS_SELECTOR)
    val answers = htmlDocument.select(TOPIC_ANSWERS_SELECTOR)
    val ratings = htmlDocument.select(TOPIC_UQ_SELECTOR)
    val dates = htmlDocument.select(TOPIC_DATES_SELECTOR)

    val topicsCount = titles.size

    assert(topicsCount == authors.size)
    assert(topicsCount == answers.size)
    assert(topicsCount == ratings.size)
    assert(topicsCount == dates.size)

    val result = ArrayList<TopicItem>()

    // Iterate through all elements and create TopicItem objects

    for (index in 0..topicsCount - 1) {

      // Parse title block
      val topicId = titles[index].attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()
      val topicTitle = titles[index].text()

      // Parse author block
      val userId = authors[index].attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()
      val nickname = authors[index].text()

      // Parse answers count
      val answersCount = answers[index].text().toInt()

      // Parse UQ
      val rating = ratings[index].text().toInt()

      // Parse date
      val topicDate = dates[index].html().extractDate()

      // Build TopicItem
      val userInfo = UserProfileShort(
          id = userId,
          name = nickname)

      val topicItem = TopicItem(
          id = topicId,
          title = topicTitle,
          answers = answersCount,
          uq = rating,
          author = userInfo,
          date = topicDate)

      result.add(topicItem)
    }

    return result
  }
}