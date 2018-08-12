package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.IncubatorItem
import io.reactivex.Single

interface IncubatorRepository {

    fun getIncubatorTopics(page: Int): Single<List<IncubatorItem>>
}
