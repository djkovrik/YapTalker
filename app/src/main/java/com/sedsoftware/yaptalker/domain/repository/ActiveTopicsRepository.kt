package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting active topics related data.
 */
interface ActiveTopicsRepository {

  fun getActiveTopics(hash: String, page: Int): Observable<BaseEntity>
}
