package com.sedsoftware.yaptalker.data.service

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.QuotedPostMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.GetPostQuotedTextService
import io.reactivex.Observable
import javax.inject.Inject

class YapGetPostQuotedTextService @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: QuotedPostMapper
) : GetPostQuotedTextService {

  override fun requestPostTextAsQuote(forumId: Int, topicId: Int, targetPostId: Int): Observable<BaseEntity> =
      dataLoader
          .loadTargetPostQuotedText(forumId, topicId, targetPostId)
          .map { quotedText -> dataMapper.transform(quotedText) }
}
