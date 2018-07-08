package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import com.sedsoftware.yaptalker.domain.repository.BlacklistRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class BlacklistInteractor @Inject constructor(
    private val repository: BlacklistRepository
) {

    fun getBlacklistedTopics(): Single<List<BlacklistedTopic>> =
        repository
            .getBlacklistedTopics()

    fun addTopicToBlacklist(name: String, id: Int): Completable =
        repository
            .addTopicToBlacklist(BlacklistedTopic(topicName = name, topicId = id))

    fun removeTopicFromBlacklistById(id: Int): Completable =
        repository
            .removeTopicFromBlacklistById(id)

    fun clearTopicsBlacklist(): Completable =
        repository
            .clearTopicsBlacklist()

    fun clearMonthOldTopicsBlacklist(): Completable =
        repository
            .clearMonthOldTopicsBlacklist()
}
