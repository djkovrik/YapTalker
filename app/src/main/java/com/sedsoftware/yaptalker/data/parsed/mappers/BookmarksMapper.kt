package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.BookmarksParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import javax.inject.Inject

/**
 * Mapper class used to transform parsed bookmarks from the data layer into BaseEntity list in the domain layer.
 */
class BookmarksMapper @Inject constructor() {

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