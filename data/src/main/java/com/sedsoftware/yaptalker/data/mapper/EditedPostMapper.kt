package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.EditedPostParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform edited post from the data layer into BaseEntity in the domain layer.
 */
class EditedPostMapper @Inject constructor() : Function<EditedPostParsed, BaseEntity> {

  override fun apply(from: EditedPostParsed): BaseEntity =
    EditedPost(text = from.editedText)
}
