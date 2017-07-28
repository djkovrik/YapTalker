package com.sedsoftware.yaptalker.data.remote.converters

import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.PostItem
import com.sedsoftware.yaptalker.data.UserProfile
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class ChosenTopicResponseBodyConverter : Converter<ResponseBody, List<PostItem>> {

  companion object {
    private val TOPIC_TITLE_SELECTOR = "h1.subpage > a"
    private val TOPIC_UQ_SELECTOR = "div.rating-value"
    private val POST_LIST_SELECTOR = "table[width='100%'][border='0'][cellspacing='1'][cellpadding='3']"
    private val POST_DATE_SELECTOR = "div.desc > a"
    private val POST_RANK_SELECTOR = "span[class~=rank-*]"
    private val POST_ID_SELECTOR = "td[align] > a"
    private val POST_AUTHOR_NICKNAME_SELECTOR = "td[align] > a + span"
    private val POST_AUTHOR_ID_SELECTOR = "a[title='Профиль']"
    private val POST_AUTHOR_REG_SELECTOR = "div[align*=left][style*=padding-left]"
    private val POST_CONTENT_SELECTOR = "td[width*=100%][valign*=top]"
    private val LINK_BY_ATTRIBUTE_SELECTOR = "href"
    private val IMG_BY_TAG_SELECTOR = "img"
    private val IMG_BY_ATTRIBUTE_SELECTOR = "src"
    private val RANK_BY_ATTRIBUTE_SELECTOR = "name"

    private val AVATAR_PREFIX = "http:"
    private val NO_AVATAR_LINK = "http://www.yaplakal.com/html/static/noavatar.gif"
  }

  override fun convert(value: ResponseBody): List<PostItem> {

    val html = value.string()
    val htmlDocument = Jsoup.parse(html)

    val result = ArrayList<PostItem>()

    val posts = htmlDocument.select(POST_LIST_SELECTOR)

    // Iterate through all elements and create PostItem objects
    for (index in 0..posts.size - 1) {

      val postDate = posts[index].select(POST_DATE_SELECTOR).text()
      val postRankText = posts[index].select(POST_RANK_SELECTOR).text()

      val postRank = when {
        postRankText.isEmpty() -> 0
        else -> postRankText.toInt()
      }

      val postId = posts[index]
          .select(POST_ID_SELECTOR)
          .attr(RANK_BY_ATTRIBUTE_SELECTOR)
          .getLastDigits()

      val authorNickname = posts[index].select(POST_AUTHOR_NICKNAME_SELECTOR).text()
      val postContent = posts[index].select(POST_CONTENT_SELECTOR).html()

      val info = posts[index]
          .select(POST_AUTHOR_ID_SELECTOR)
          .first()
          .getElementsByAttribute(LINK_BY_ATTRIBUTE_SELECTOR).first()

      val authorId = info.attr(LINK_BY_ATTRIBUTE_SELECTOR).getLastDigits()
      val avatarLink = info.getElementsByTag(IMG_BY_TAG_SELECTOR).first()
      val authorAvatar = avatarLink?.attr(IMG_BY_ATTRIBUTE_SELECTOR) ?: NO_AVATAR_LINK
      val authorStatusDesc = posts[index].select(POST_AUTHOR_REG_SELECTOR).html()

      val user = UserProfile(
          id = authorId,
          name = authorNickname,
          avatar = "$AVATAR_PREFIX$authorAvatar",
          registered = authorStatusDesc)

      val postItem = PostItem(
          id = postId,
          author = user,
          date = postDate,
          uq = postRank,
          content = postContent)

      result.add(postItem)
    }

    return result
  }
}
