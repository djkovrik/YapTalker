package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.QuotedPostParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
import javax.inject.Inject

/**
 * Mapper class used to transform quoted post from the data layer into BaseEntity in the domain layer.
 */
class QuotedPostMapper @Inject constructor() {

  fun transform(quotedPost: QuotedPostParsed): BaseEntity =
      QuotedPost(
          text = quotedPost.quotedText
      )
}
