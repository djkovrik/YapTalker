package com.sedsoftware.yaptalker.data.remote.converters

import com.sedsoftware.yaptalker.commons.extensions.extractDate
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.ForumItem
import com.sedsoftware.yaptalker.data.TopicItemShort
import com.sedsoftware.yaptalker.data.UserProfileShort
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class ForumsListResponseBodyConverter : Converter<ResponseBody, List<ForumItem>> {

  companion object {
    private val HEADERS_SELECTOR = "td.row4"
    private val FORUM_TITLES_SELECTOR = "b > a[href].title"
    private val LINK_BY_ATTRIBUTE_SELECTOR = "href"
    private val LAST_TOPIC_SELECTOR = "td.row2 > span"

    // Defaults for not parsed values
    private val STRING_DEFAULT = "Unknown forum item value"
  }

  override fun convert(value: ResponseBody): List<ForumItem> {

    val html = value.string()
    val htmlDocument = Jsoup.parse(html)

    val headers = htmlDocument.select(HEADERS_SELECTOR)
    val titles = headers.select(FORUM_TITLES_SELECTOR)
    val topics = htmlDocument.select(LAST_TOPIC_SELECTOR)

    val forumsCount = titles.size

    assert(forumsCount == topics.size)

    val result = ArrayList<ForumItem>()

    // Iterate through all elements and create ForumItem objects

    for (index in 0..forumsCount - 1) {

      // Parse title block
      val forumId = titles[index].attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()
      val forumTitle = titles[index]?.text() ?: STRING_DEFAULT

      // Parse last topic info
      val links = topics[index].getElementsByAttribute(LINK_BY_ATTRIBUTE_SELECTOR)
      val topicId = links[1].attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()
      val topicTitle = links[1]?.text() ?: STRING_DEFAULT
      val userId = links[2].attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()
      val nickname = links[2]?.text() ?: STRING_DEFAULT

      val lastPostDate = topics[index]?.html()?.extractDate() ?: STRING_DEFAULT

      // Construct ForumItem object
      val userInfo = UserProfileShort(
          id = userId,
          name = nickname)

      val lastTopicItem = TopicItemShort(
          id = topicId,
          title = topicTitle,
          author = userInfo,
          date = lastPostDate)

      val forumItem = ForumItem(
          id = forumId,
          title = forumTitle,
          lastTopic = lastTopicItem)

      result.add(forumItem)
    }

    return result
  }
}
