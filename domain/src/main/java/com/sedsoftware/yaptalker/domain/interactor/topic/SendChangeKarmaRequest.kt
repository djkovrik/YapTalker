package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Single
import javax.inject.Inject

class SendChangeKarmaRequest @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) : SingleUseCaseWithParameter<SendChangeKarmaRequest.Params, BaseEntity> {

  override fun execute(parameter: Params): Single<BaseEntity> =
      chosenTopicRepository
          .requestKarmaChange(parameter.isTopic, parameter.targetPostId, parameter.targetTopicId, parameter.diff)

  class Params(val isTopic: Boolean, val targetPostId: Int, val targetTopicId: Int, val diff: Int)
}
