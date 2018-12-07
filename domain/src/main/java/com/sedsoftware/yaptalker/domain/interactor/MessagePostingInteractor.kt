package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Completable
import javax.inject.Inject

class MessagePostingInteractor @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) {

    fun sendMessageRequest(forumId: Int,
                           topicId: Int,
                           page: Int,
                           authKey: String,
                           message: String,
                           filePath: String = ""): Completable =
        chosenTopicRepository
            .requestMessageSending(forumId, topicId, page, authKey, message, filePath)

    fun sendEditedMessageRequest(forumId: Int,
                                 topicId: Int,
                                 postId: Int,
                                 page: Int,
                                 authKey: String,
                                 message: String, file: String = ""): Completable =
        chosenTopicRepository
            .requestEditedMessageSending(forumId, topicId, postId, page, authKey, message, file)
}
