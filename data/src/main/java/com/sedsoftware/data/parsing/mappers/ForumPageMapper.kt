package com.sedsoftware.data.parsing.mappers

import com.sedsoftware.data.parsing.ForumPageParsed
import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.base.ForumInfoBlock
import com.sedsoftware.domain.entity.base.NavigationPanel
import com.sedsoftware.domain.entity.base.Topic

/**
 * Mapper class used to transform parsed forum page from the data layer into YapEntity list in the domain layer.
 */
class ForumPageMapper {

  companion object {
    private const val TOPICS_PER_PAGE = 30
  }

  fun transform(forumPage: ForumPageParsed): List<BaseEntity> {

    val result: MutableList<BaseEntity> = ArrayList(TOPICS_PER_PAGE)

    with(forumPage) {
      result.add(ForumInfoBlock(
          forumTitle = forumTitle,
          forumId = forumId.toInt()
      ))

      result.add(NavigationPanel(
          currentPage = navigation.currentPage.toInt(),
          totalPages = navigation.totalPages.toInt()
      ))

      topics.forEach { topic ->
        result.add(Topic(
            title = topic.title,
            link = topic.link,
            isPinned = topic.isPinned.isNotEmpty(),
            isClosed = topic.isClosed.isNotEmpty(),
            author = topic.author,
            authorLink = topic.authorLink,
            rating = topic.rating.toInt(),
            answers = topic.answers.toInt(),
            lastPostDate = topic.lastPostDate,
            lastPostAuthor = topic.lastPostAuthor
        ))
      }
    }

    return result
  }
}
