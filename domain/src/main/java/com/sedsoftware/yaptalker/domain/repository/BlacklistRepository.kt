package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import io.reactivex.Completable
import io.reactivex.Single

interface BlacklistRepository {
    fun getBlacklistedTopics(): Single<List<BlacklistedTopic>>
    fun addTopicToBlacklist(topic: BlacklistedTopic): Completable
    fun removeTopicFromBlacklistById(id: Int): Completable
    fun clearTopicsBlacklist(): Completable
    fun clearMonthOldTopicsBlacklist(): Completable
}
