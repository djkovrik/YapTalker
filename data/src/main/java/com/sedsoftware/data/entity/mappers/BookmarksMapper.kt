package com.sedsoftware.data.entity.mappers

import com.sedsoftware.data.entity.BookmarksParsed
import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.base.BookmarkedTopic

/**
 * Mapper class used to transform parsed bookmarks from the data layer into YapEntity list in the domain layer.
 */
class BookmarksMapper {

  fun transform(bookmarks: BookmarksParsed): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList()

    bookmarks.topics.forEach { topic ->
      result.add(BookmarkedTopic(
          bookmarkId = topic.bookmarkId.toInt(),
          title = topic.title,
          link = topic.link
      ))
    }

    return result
  }
}