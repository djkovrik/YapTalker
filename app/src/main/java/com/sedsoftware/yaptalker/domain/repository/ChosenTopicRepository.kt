package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting chosen topic data.
 */
interface ChosenTopicRepository {

  fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Observable<List<BaseEntity>>
}
