package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Single
import javax.inject.Inject

class GetChosenTopicGallery @Inject constructor(
  private val chosenTopicRepository: ChosenTopicRepository
) : SingleUseCaseWithParameter<GetChosenTopicGallery.Params, List<BaseEntity>> {

  override fun execute(parameter: Params): Single<List<BaseEntity>> =
    chosenTopicRepository
      .getChosenTopic(parameter.forumId, parameter.topicId, parameter.startPage)

  class Params(val forumId: Int, val topicId: Int, val startPage: Int)
}
