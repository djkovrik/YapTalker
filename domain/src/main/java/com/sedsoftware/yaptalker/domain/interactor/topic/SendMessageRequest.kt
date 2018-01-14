package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Completable
import javax.inject.Inject

class SendMessageRequest @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) : CompletableUseCaseWithParameter<SendMessageRequest.Params> {

  override fun execute(parameter: Params): Completable =
      chosenTopicRepository
          .requestMessageSending(
              targetForumId = parameter.forumId,
              targetTopicId = parameter.topicId,
              page = parameter.page,
              authKey = parameter.authKey,
              message = parameter.message
          )

  class Params(val forumId: Int, val topicId: Int, val page: Int, val authKey: String, val message: String)
}
