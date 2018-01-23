package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting chosen forum data.
 */
interface ChosenForumRepository {

  fun getChosenForum(forumId: Int, startPageNumber: Int, sortingMode: String): Observable<List<BaseEntity>>
}
