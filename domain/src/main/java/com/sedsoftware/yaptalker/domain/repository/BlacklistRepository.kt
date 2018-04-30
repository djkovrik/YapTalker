package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import io.reactivex.Completable
import io.reactivex.Single

interface BlacklistRepository {

  fun getBlacklistedTopics(): Single<List<BlacklistedTopic>>

  fun addTopicToBlacklist(topic: BlacklistedTopic): Completable

  fun addTopicsToBlacklist(topics: List<BlacklistedTopic>): Completable

  fun removeTopicFromBlacklistByLink(link: String): Completable

  fun removeTopicFromBlacklistByName(name: String): Completable

  fun clearTopicsBlacklist(): Completable

  fun clearMonthOldTopicsBlacklist(): Completable
}
