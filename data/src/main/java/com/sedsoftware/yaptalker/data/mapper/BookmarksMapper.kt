package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.BookmarksParsed
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import io.reactivex.functions.Function
import javax.inject.Inject

class BookmarksMapper @Inject constructor() : Function<BookmarksParsed, List<BookmarkedTopic>> {

  override fun apply(from: BookmarksParsed): List<BookmarkedTopic> =
    from.topics
      .map { parsedTopic ->
        BookmarkedTopic(
          bookmarkId = parsedTopic.bookmarkId.toInt(),
          title = parsedTopic.title,
          link = parsedTopic.link
        )
      }
}
