package com.sedsoftware.data.parsing.mappers

import com.sedsoftware.data.parsing.BookmarksParsed
import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.base.BookmarkedTopic

/**
 * Mapper class used to transform parsed bookmarks from the data layer into BaseEntity list in the domain layer.
 */
class BookmarksMapper {

  fun transform(bookmarks: BookmarksParsed): List<BaseEntity> {

    val result: MutableList<BaseEntity> = ArrayList()

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