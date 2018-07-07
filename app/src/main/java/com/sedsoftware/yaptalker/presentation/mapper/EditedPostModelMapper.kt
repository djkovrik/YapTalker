package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import com.sedsoftware.yaptalker.presentation.model.base.EditedPostModel
import io.reactivex.functions.Function
import javax.inject.Inject

class EditedPostModelMapper @Inject constructor() : Function<EditedPost, EditedPostModel> {

    override fun apply(item: EditedPost): EditedPostModel =
        EditedPostModel(text = item.text)
}
