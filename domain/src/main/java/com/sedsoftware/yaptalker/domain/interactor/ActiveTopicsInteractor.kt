package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ActiveTopicsRepository
import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import io.reactivex.Single
import javax.inject.Inject

class ActiveTopicsInteractor @Inject constructor(
    private val activeTopicsRepository: ActiveTopicsRepository,
    private val searchIdRepository: SearchIdRepository
) {

    fun getActiveTopics(hash: String, page: Int): Single<List<BaseEntity>> =
        activeTopicsRepository
            .getActiveTopics(hash, page)

    fun getSearchId(): Single<String> =
        searchIdRepository
            .getSearchId()
}
