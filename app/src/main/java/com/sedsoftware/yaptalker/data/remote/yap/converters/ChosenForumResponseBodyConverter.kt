package com.sedsoftware.yaptalker.data.remote.yap.converters

import com.sedsoftware.yaptalker.commons.extensions.extractDate
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.model.TopicItemList
import com.sedsoftware.yaptalker.data.model.UserProfileShort
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class ChosenForumResponseBodyConverter : Converter<ResponseBody, List<TopicItemList>> {

  companion object {
    private val TOPIC_TITLES_SELECTOR = "td.row4 > div > a"
    private val TOPIC_AUTHORS_SELECTOR = "td.row2 > a"
    private val TOPIC_ANSWERS_SELECTOR = "td[align].row4 + td[align].row2 + td.row4 + td[align].row2 + td[align].row4"
    private val TOPIC_UQ_SELECTOR = "div.rating-short-value"
    private val TOPIC_DATES_SELECTOR = "td.row2 > span.desc"
    private val LINK_BY_ATTRIBUTE_SELECTOR = "href"

    // Defaults for not parsed values
    private val STRING_DEFAULT = "Unknown topic item value"
    private val INT_DEFAULT = -1
  }

  override fun convert(value: ResponseBody): List<TopicItemList> {

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

    val result = ArrayList<TopicItemList>()

    // Iterate through all elements and create TopicItem objects

    for (index in 0..topicsCount - 1) {

      // Parse title block
      val topicId = titles[index]?.attr(LINK_BY_ATTRIBUTE_SELECTOR)?.getLastDigits() ?: INT_DEFAULT
      val topicTitle = titles[index]?.text() ?: STRING_DEFAULT

      // Parse author block
      val userId = authors[index]?.attr(LINK_BY_ATTRIBUTE_SELECTOR)?.getLastDigits() ?: INT_DEFAULT
      val nickname = authors[index]?.text() ?: STRING_DEFAULT

      // Parse answers count
      val answersCount = answers[index]?.text()?.toInt() ?: INT_DEFAULT

      // Parse UQ
      val rating = ratings[index]?.text()?.toInt() ?: INT_DEFAULT

      // Parse date
      val topicDate = dates[index]?.html()?.extractDate() ?: STRING_DEFAULT

      // Build TopicItem
      val userInfo = UserProfileShort(
          id = userId,
          name = nickname)

      val topicItem = TopicItemList(
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
