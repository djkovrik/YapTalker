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

  fun addTopicToBlacklist(topic: BlacklistedTopic): Completable =
    repository
      .addTopicToBlacklist(topic)

  fun addTopicsToBlacklist(topics: List<BlacklistedTopic>): Completable =
    repository
      .addTopicsToBlacklist(topics)

  fun removeTopicFromBlacklistByLink(link: String): Completable =
    repository
      .removeTopicFromBlacklistByLink(link)

  fun removeTopicFromBlacklistByName(name: String): Completable =
    repository
      .removeTopicFromBlacklistByName(name)

  fun clearTopicsBlacklist(): Completable =
    repository
      .clearTopicsBlacklist()

  fun clearMonthOldTopicsBlacklist(): Completable =
    repository
      .clearMonthOldTopicsBlacklist()
}
