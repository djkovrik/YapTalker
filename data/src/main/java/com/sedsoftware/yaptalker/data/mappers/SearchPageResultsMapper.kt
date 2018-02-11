package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.data.parsed.SearchTopicsPageParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.SearchTopicItem
import com.sedsoftware.yaptalker.domain.entity.base.SearchTopicsPageInfo
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class SearchPageResultsMapper @Inject constructor() : Function<SearchTopicsPageParsed, List<BaseEntity>> {

  companion object {
    private const val TOPICS_PER_PAGE = 25
  }

  override fun apply(from: SearchTopicsPageParsed): List<BaseEntity> {

    val result: MutableList<BaseEntity> = ArrayList(TOPICS_PER_PAGE)

    with(from) {

      result.add(
        SearchTopicsPageInfo(
          hasNextPage = hasNextPage.isNotEmpty(),
          searchId = searchId
        )
      )

      topics.forEach { topic ->
        result.add(
          SearchTopicItem(
            title = topic.title,
            link = topic.link,
            isPinned = topic.isPinned.isNotEmpty(),
            isClosed = topic.isClosed.isNotEmpty(),
            forumTitle = topic.forumTitle,
            forumLink = topic.forumLink,
            rating = topic.rating.toInt(),
            answers = topic.answers.toInt(),
            lastPostDate = topic.lastPostDate
          )
        )
      }
    }

    return result
  }
}
