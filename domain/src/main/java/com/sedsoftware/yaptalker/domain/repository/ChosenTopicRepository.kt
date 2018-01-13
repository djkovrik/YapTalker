package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting chosen topic data.
 */
interface ChosenTopicRepository {

  fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Observable<List<BaseEntity>>

  fun requestPostTextAsQuote(forumId: Int, topicId: Int, targetPostId: Int): Observable<BaseEntity>

  fun requestTopicKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Observable<BaseEntity>

  fun requestPostKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Observable<BaseEntity>

  fun requestMessageSending(
      targetForumId: Int, targetTopicId: Int, page: Int, authKey: String, message: String): Observable<BaseEntity>
}
