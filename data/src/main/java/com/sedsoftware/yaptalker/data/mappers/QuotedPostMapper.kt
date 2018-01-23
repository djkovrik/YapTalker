package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.data.parsed.QuotedPostParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform quoted post from the data layer into BaseEntity in the domain layer.
 */
class QuotedPostMapper @Inject constructor() : Function<QuotedPostParsed, BaseEntity> {

  override fun apply(from: QuotedPostParsed): BaseEntity =
    QuotedPost(text = from.quotedText)
}
