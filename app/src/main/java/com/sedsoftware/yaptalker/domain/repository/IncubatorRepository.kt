package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting incubator related data.
 */
interface IncubatorRepository {

  fun getIncubatorTopics(page: Int): Observable<BaseEntity>
}
