package com.sedsoftware.yaptalker.domain.service

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents karma changing service.
 */
interface KarmaService {

  fun requestTopicKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Observable<BaseEntity>

  fun requestPostKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Observable<BaseEntity>
}
