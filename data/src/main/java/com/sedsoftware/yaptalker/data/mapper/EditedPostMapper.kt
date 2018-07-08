package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.EditedPostParsed
import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import io.reactivex.functions.Function
import javax.inject.Inject

class EditedPostMapper @Inject constructor() : Function<EditedPostParsed, EditedPost> {

    override fun apply(from: EditedPostParsed): EditedPost =
        EditedPost(text = from.editedText)
}
