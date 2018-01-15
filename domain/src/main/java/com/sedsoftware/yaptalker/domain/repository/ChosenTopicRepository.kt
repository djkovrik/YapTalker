package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that represents a Repository for getting chosen topic data.
 */
interface ChosenTopicRepository {

  fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Single<List<BaseEntity>>

  fun requestPostTextAsQuote(forumId: Int, topicId: Int, targetPostId: Int): Single<BaseEntity>

  fun requestPostTextForEditing(forumId: Int, topicId: Int, targetPostId: Int, startingPost: Int): Single<BaseEntity>

  fun requestKarmaChange(isTopic: Boolean, targetPostId: Int, targetTopicId: Int, diff: Int): Single<BaseEntity>

  fun requestPostKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Single<BaseEntity>

  fun requestTopicKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Single<BaseEntity>

  fun requestMessageSending(
      targetForumId: Int, targetTopicId: Int, page: Int, authKey: String, message: String): Completable
}
