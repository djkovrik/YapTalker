package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.IncubatorItem
import io.reactivex.Observable

interface IncubatorRepository {

  fun getIncubatorTopics(page: Int): Observable<IncubatorItem>
}
