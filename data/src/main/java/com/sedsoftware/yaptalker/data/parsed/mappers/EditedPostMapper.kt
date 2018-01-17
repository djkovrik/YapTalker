package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.EditedPostParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import javax.inject.Inject

/**
 * Mapper class used to transform edited post from the data layer into BaseEntity in the domain layer.
 */
class EditedPostMapper @Inject constructor() {

  fun transform(editedPost: EditedPostParsed): BaseEntity =
    EditedPost(
      text = editedPost.editedText
    )
}
