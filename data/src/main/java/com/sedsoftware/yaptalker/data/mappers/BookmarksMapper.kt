package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.data.parsed.BookmarksParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform parsed bookmarks from the data layer into BaseEntity list in the domain layer.
 */
class BookmarksMapper @Inject constructor() : Function<BookmarksParsed, List<BaseEntity>> {

  override fun apply(from: BookmarksParsed): List<BaseEntity> =
    from
      .topics
      .map { parsedTopic ->
        BookmarkedTopic(
          bookmarkId = parsedTopic.bookmarkId.toInt(),
          title = parsedTopic.title,
          link = parsedTopic.link
        )
      }
}
