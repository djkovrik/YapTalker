package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ActiveTopic
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
            .getBlacklistedTopicIds()
            .flatMap { blacklistedIds ->
                activeTopicsRepository
                    .getActiveTopics(hash, page)
                    .map { list -> list.filter { item -> blacklistTopicChecker(blacklistedIds, item) } }
            }


    fun getSearchId(): Single<String> =
        searchIdRepository
            .getSearchId()

    private fun blacklistTopicChecker(blacklistedIds: Set<Int>, item: BaseEntity): Boolean =
        if (item is ActiveTopic) !blacklistedIds.contains(item.id) else true
}
