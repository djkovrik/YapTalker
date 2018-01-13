package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Single
import javax.inject.Inject

class SendChangeKarmaRequestPost @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) : SingleUseCaseWithParameter<SendChangeKarmaRequestPost.Params, BaseEntity> {

  override fun execute(parameter: Params): Single<BaseEntity> =
      chosenTopicRepository
          .requestPostKarmaChange(parameter.targetTopicId, parameter.targetPostId, parameter.diff)

  class Params(val targetPostId: Int, val targetTopicId: Int, val diff: Int)
}
