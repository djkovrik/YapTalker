package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Single
import javax.inject.Inject

class GetQuotedText @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) : SingleUseCaseWithParameter<GetQuotedText.Params, BaseEntity> {

  override fun execute(parameter: Params): Single<BaseEntity> =
      chosenTopicRepository
          .requestPostTextAsQuote(parameter.forumId, parameter.topicId, parameter.targetPostId)

  class Params(val forumId: Int, val topicId: Int, val targetPostId: Int)
}
