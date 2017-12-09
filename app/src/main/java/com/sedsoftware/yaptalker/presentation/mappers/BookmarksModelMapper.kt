package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel

/**
 * Mapper class used to transform bookmarks entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class BookmarksModelMapper {

  fun transform(items: List<BaseEntity>): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList(items.size)

    items.forEach { item ->
      if (item is BookmarkedTopic) {
        result.add(BookmarkedTopicModel(
            bookmarkId = item.bookmarkId,
            title = item.title,
            link = item.link
        ))
      }
    }

    return result
  }
}