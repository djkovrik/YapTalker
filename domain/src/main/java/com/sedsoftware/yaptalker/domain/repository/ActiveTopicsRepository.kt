package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Single

interface ActiveTopicsRepository {
    fun getActiveTopics(hash: String, page: Int): Single<List<BaseEntity>>
    fun getBlacklistedTopicIds(): Single<Set<Int>>
}
