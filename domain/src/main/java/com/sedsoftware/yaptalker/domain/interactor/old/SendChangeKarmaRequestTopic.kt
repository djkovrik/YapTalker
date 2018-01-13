package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendChangeKarmaRequestTopic.Params
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Observable
import javax.inject.Inject

class SendChangeKarmaRequestTopic @Inject constructor(
    private val topicRepository: ChosenTopicRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      topicRepository
          .requestTopicKarmaChange(params.targetPostId, params.targetTopicId, params.diff)

  class Params(val targetPostId: Int, val targetTopicId: Int, val diff: Int)
}
