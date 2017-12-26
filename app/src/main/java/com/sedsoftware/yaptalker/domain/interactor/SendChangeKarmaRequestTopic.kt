package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.KarmaService
import io.reactivex.Observable
import javax.inject.Inject

class SendChangeKarmaRequestTopic @Inject constructor(
    private val karmaService: KarmaService
) : UseCase<BaseEntity, SendChangeKarmaRequestTopic.Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      karmaService
          .requestTopicKarmaChange(params.targetPostId, params.targetTopicId, params.diff)

  class Params(val targetPostId: Int, val targetTopicId: Int, val diff: Int)
}
