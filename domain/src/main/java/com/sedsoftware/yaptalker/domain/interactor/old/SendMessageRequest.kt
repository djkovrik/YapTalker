package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendMessageRequest.Params
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Observable
import javax.inject.Inject

class SendMessageRequest @Inject constructor(
    private val topicRepository: ChosenTopicRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      topicRepository
          .requestMessageSending(
              targetForumId = params.forumId,
              targetTopicId = params.topicId,
              page = params.page,
              authKey = params.authKey,
              message = params.message)

  class Params(val forumId: Int, val topicId: Int, val page: Int, val authKey: String, val message: String)
}
