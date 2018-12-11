package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import io.reactivex.Completable
import io.reactivex.Single

interface ChosenTopicRepository {

    fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Single<List<BaseEntity>>

    fun requestPostTextAsQuote(forumId: Int, topicId: Int, targetPostId: Int): Single<QuotedPost>

    fun requestPostTextForEditing(forumId: Int, topicId: Int, postId: Int, startingPost: Int): Single<EditedPost>

    fun requestKarmaChange(isTopic: Boolean, targetPostId: Int, targetTopicId: Int, diff: Int): Single<ServerResponse>

    fun requestPostKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Single<ServerResponse>

    fun requestTopicKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Single<ServerResponse>

    fun requestMessageSending(
        targetForumId: Int,
        targetTopicId: Int,
        page: Int,
        authKey: String,
        message: String,
        filePath: String
    ): Completable

    fun requestEditedMessageSending(
        targetForumId: Int,
        targetTopicId: Int,
        targetPostId: Int,
        page: Int,
        authKey: String,
        message: String,
        file: String
    ): Completable
}
