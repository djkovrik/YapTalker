package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.QuotedPostParsed
import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
import io.reactivex.functions.Function
import javax.inject.Inject

class QuotedPostMapper @Inject constructor() : Function<QuotedPostParsed, QuotedPost> {

    override fun apply(from: QuotedPostParsed): QuotedPost =
        QuotedPost(text = from.quotedText)
}
