package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
import com.sedsoftware.yaptalker.presentation.model.base.QuotedPostModel
import io.reactivex.functions.Function
import javax.inject.Inject

class QuotedPostModelMapper @Inject constructor() : Function<QuotedPost, QuotedPostModel> {

  override fun apply(item: QuotedPost): QuotedPostModel =
    QuotedPostModel(text = item.text)
}
