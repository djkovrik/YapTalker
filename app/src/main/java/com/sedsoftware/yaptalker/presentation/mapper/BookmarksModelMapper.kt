package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import io.reactivex.functions.Function
import javax.inject.Inject

class BookmarksModelMapper @Inject constructor() : Function<BookmarkedTopic, BookmarkedTopicModel> {

    override fun apply(item: BookmarkedTopic): BookmarkedTopicModel =
        BookmarkedTopicModel(
            bookmarkId = item.bookmarkId,
            title = item.title,
            link = item.link
        )
}
