package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform bookmarks entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class BookmarksModelMapper @Inject constructor() : Function<BaseEntity, YapEntity> {

  override fun apply(item: BaseEntity): YapEntity {
    item as BookmarkedTopic

    return BookmarkedTopicModel(
      bookmarkId = item.bookmarkId,
      title = item.title,
      link = item.link
    )
  }
}
