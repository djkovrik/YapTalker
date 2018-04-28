package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.ForumsListParsed
import com.sedsoftware.yaptalker.domain.entity.base.Forum
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class ForumsListMapper @Inject constructor() : Function<ForumsListParsed, List<Forum>> {

  companion object {
    private const val FORUMS_COUNT = 40
  }

  override fun apply(from: ForumsListParsed): List<Forum> {

    val result: MutableList<Forum> = ArrayList(FORUMS_COUNT)

    with(from) {
      check(titles.size == ids.size) { "Titles list size should match ids list size" }
      check(ids.size == topics.size) { "Topics list size should match ids list size" }

      titles.forEachIndexed { index, _ ->
        result.add(
          Forum(
            title = titles[index],
            forumId = ids[index].toInt(),
            lastTopicTitle = topics[index].title,
            lastTopicAuthor = topics[index].author,
            date = topics[index].date
          )
        )
      }
    }

    return result
  }
}
