package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.EditedPostModel
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform edited post text from the domain layer into YapEntity in the presentation layer.
 */
class EditedPostModelMapper @Inject constructor() : Function<BaseEntity, YapEntity> {

  override fun apply(item: BaseEntity): YapEntity {

    item as EditedPost

    return EditedPostModel(text = item.text)
  }
}
