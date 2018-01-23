package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.Forum
import com.sedsoftware.yaptalker.presentation.mappers.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel
import javax.inject.Inject

/**
 * Mapper class used to transform forums list entities from the domain layer into YapEntity list
 * in the presentation layer.
 */
class ForumsListModelMapper @Inject constructor(
  private val dateTransformer: DateTransformer
) {

  fun transform(item: BaseEntity): YapEntity {

    item as Forum

    return ForumModel(
      title = item.title,
      forumId = item.forumId,
      lastTopicTitle = item.lastTopicTitle,
      lastTopicAuthor = item.lastTopicAuthor,
      date = dateTransformer.transformDateToShortView(item.date)
    )
  }
}
