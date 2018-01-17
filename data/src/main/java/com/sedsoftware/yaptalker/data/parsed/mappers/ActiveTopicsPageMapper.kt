package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.ActiveTopicsPageParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ActiveTopic
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import java.util.ArrayList
import javax.inject.Inject

/**
 * Mapper class used to transform parsed active topics page from the data layer into BaseEntity list
 * in the domain layer.
 */
class ActiveTopicsPageMapper @Inject constructor() {

  companion object {
    private const val TOPICS_PER_PAGE = 25
  }

  fun transform(activeTopicsPage: ActiveTopicsPageParsed): List<BaseEntity> {

    val result: MutableList<BaseEntity> = ArrayList(TOPICS_PER_PAGE)

    with(activeTopicsPage) {
      topics.forEach { topic ->
        result.add(
          ActiveTopic(
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

      result.add(
        NavigationPanel(
          currentPage = navigation.currentPage.toInt(),
          totalPages = navigation.totalPages.toInt()
        )
      )
    }

    return result
  }
}
