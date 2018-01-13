package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Single
import javax.inject.Inject

class SendChangeKarmaRequestTopic @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) : SingleUseCaseWithParameter<SendChangeKarmaRequestTopic.Params, BaseEntity> {

  override fun execute(parameter: Params): Single<BaseEntity> =
      chosenTopicRepository
          .requestTopicKarmaChange(parameter.targetPostId, parameter.targetTopicId, parameter.diff)

  class Params(val targetPostId: Int, val targetTopicId: Int, val diff: Int)
}
