package com.sedsoftware.yaptalker.domain.service

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents post quoting service.
 */
interface GetPostQuotedTextService {

  fun requestPostTextAsQuote(forumId: Int, topicId: Int, targetPostId: Int): Observable<BaseEntity>
}
