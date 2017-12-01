package com.sedsoftware.data.entity.mappers

import com.sedsoftware.data.entity.ActiveTopicsPageParsed
import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.base.ActiveTopic
import com.sedsoftware.domain.entity.base.NavigationPanel

/**
 * Mapper class used to transform parsed active topics page from the data layer into YapEntity list
 * in the domain layer.
 */
class ActiveTopicsPageMapper {

  companion object {
    private const val TOPICS_PER_PAGE = 25
  }

  fun transform(activeTopicsPage: ActiveTopicsPageParsed): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList(TOPICS_PER_PAGE)

    with(activeTopicsPage) {
      topics.forEach { topic ->
        result.add(ActiveTopic(
            title = topic.title,
            link = topic.link,
            isPinned = topic.isPinned.isNotEmpty(),
            isClosed = topic.isClosed.isNotEmpty(),
            forumTitle = topic.forumTitle,
            forumLink = topic.forumLink,
            rating = topic.rating.toInt(),
            answers = topic.answers.toInt(),
            lastPostDate = topic.lastPostDate
        ))
      }

      result.add(NavigationPanel(
          currentPage = navigation.currentPage.toInt(),
          totalPages = navigation.totalPages.toInt()
      ))
    }

    return result
  }
}
