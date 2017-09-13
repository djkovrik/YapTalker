package com.sedsoftware.yaptalker.data.model

import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import pl.droidsonroids.jspoon.annotation.Selector

class Forums {
  @Selector("td.row4 > b > a.title") lateinit var titles: List<String>
  @Selector("td.row4 > b > a.title", attr = "href") lateinit var ids: List<String>
  @Selector("td.row2[nowrap=nowrap]") lateinit var topics: List<LastTopic>
}

class LastTopic {
  @Selector("a.subtitle", defValue = "Unknown") lateinit var title: String
  @Selector("a ~ a ~ a", defValue = "Unknown") lateinit var author: String
  @Selector(".desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown") lateinit var date: String
}

fun Forums.createForumsList(): List<ForumItem> {

  check(titles.size == ids.size) { "Titles size should match ids size" }
  check(topics.size == ids.size) { "Topics size should match ids size" }

  val result: MutableList<ForumItem> = ArrayList()

  titles.forEachIndexed { index, _ ->
    result.add(ForumItem(
        title = titles[index],
        forumId = ids[index].getLastDigits(),
        lastTopicTitle = topics[index].title,
        lastTopicAuthor = topics[index].author,
        date = topics[index].date))
  }

  return result
}

data class ForumItem(
    val title: String,
    val forumId: Int,
    val lastTopicTitle: String,
    val lastTopicAuthor: String,
    val date: String)
