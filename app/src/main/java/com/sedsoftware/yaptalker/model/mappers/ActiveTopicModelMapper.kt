package com.sedsoftware.yaptalker.model.mappers

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.base.ActiveTopic
import com.sedsoftware.domain.entity.base.NavigationPanel
import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.base.ActiveTopicModel
import com.sedsoftware.yaptalker.model.base.NavigationPanelModel

/**
 * Mapper class used to transform active topics entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class ActiveTopicModelMapper {

  fun transform(items: List<BaseEntity>): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList(items.size)

    items.forEach { item ->
      when (item) {
        is NavigationPanel -> result.add(NavigationPanelModel(
            currentPage = item.currentPage,
            totalPages = item.totalPages
        ))
        is ActiveTopic -> result.add(ActiveTopicModel(
            title = item.title,
            link = item.link,
            isPinned = item.isPinned,
            isClosed = item.isClosed,
            forumTitle = item.forumTitle,
            forumLink = item.forumLink,
            rating = item.rating,
            answers = item.answers,
            lastPostDate = item.lastPostDate
        ))
      }
    }

    return result
  }
}
