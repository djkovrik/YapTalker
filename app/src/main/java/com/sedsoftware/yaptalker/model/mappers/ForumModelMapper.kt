package com.sedsoftware.yaptalker.model.mappers

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.base.ForumInfoBlock
import com.sedsoftware.domain.entity.base.NavigationPanel
import com.sedsoftware.domain.entity.base.Topic
import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.base.ForumInfoBlockModel
import com.sedsoftware.yaptalker.model.base.NavigationPanelModel
import com.sedsoftware.yaptalker.model.base.TopicModel

/**
 * Mapper class used to transform forum page entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class ForumModelMapper {

  fun transform(items: List<BaseEntity>): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList(items.size)

    items.forEach { item ->
      when (item) {
        is ForumInfoBlock -> result.add(ForumInfoBlockModel(
            forumTitle = item.forumTitle,
            forumId = item.forumId
        ))
        is NavigationPanel -> result.add(NavigationPanelModel(
            currentPage = item.currentPage,
            totalPages = item.totalPages
        ))
        is Topic -> result.add(TopicModel(
            title = item.title,
            link = item.link,
            isPinned = item.isPinned,
            isClosed = item.isClosed,
            author = item.author,
            authorLink = item.authorLink,
            rating = item.rating,
            answers = item.answers,
            lastPostDate = item.lastPostDate,
            lastPostAuthor = item.lastPostAuthor
        ))
      }
    }

    return result
  }
}
