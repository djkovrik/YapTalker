package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting forums list data.
 */
interface ForumsListRepository {

  fun getMainForumsList(): Observable<BaseEntity>
}
