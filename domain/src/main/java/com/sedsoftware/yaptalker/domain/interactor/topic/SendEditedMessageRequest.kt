package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Completable
import javax.inject.Inject


class SendEditedMessageRequest @Inject constructor(
  private val chosenTopicRepository: ChosenTopicRepository
) : CompletableUseCaseWithParameter<SendEditedMessageRequest.Params> {

  override fun execute(parameter: Params): Completable =
    chosenTopicRepository
      .requestEditedMessageSending(
        targetTopicId = parameter.topicId,
        targetPostId = parameter.postId,
        page = parameter.page,
        authKey = parameter.authKey,
        message = parameter.message
      )

  class Params(val topicId: Int, val postId: Int, val page: Int, val authKey: String, val message: String)
}
