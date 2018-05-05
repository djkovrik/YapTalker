package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.database.mapper.BlacklistDbMapper
import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import com.sedsoftware.yaptalker.domain.repository.BlacklistRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Calendar
import javax.inject.Inject


class YapBlacklistRepository @Inject constructor(
  private val database: YapTalkerDatabase,
  private val mapper: BlacklistDbMapper
) : BlacklistRepository {

  private val monthThreeshold: Long by lazy {
    Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.time.time
  }

  override fun getBlacklistedTopics(): Single<List<BlacklistedTopic>> =
    database
      .getTopicsDao()
      .getAllTopics()
      .map { topics -> topics.map { mapper.mapFromDb(it) } }

  override fun addTopicToBlacklist(topic: BlacklistedTopic): Completable =
    Completable.fromAction {
      database
        .getTopicsDao()
        .insertTopic(mapper.mapToDb(topic))
    }

  override fun removeTopicFromBlacklistById(id: Int): Completable =
    Completable.fromAction {
      database
        .getTopicsDao()
        .deleteTopicById(id)
    }


  override fun clearTopicsBlacklist(): Completable =
    Completable.fromAction {
      database
        .getTopicsDao()
        .deleteAllTopics()
    }

  override fun clearMonthOldTopicsBlacklist(): Completable =
    Completable.fromAction {
      database
        .getTopicsDao()
        .deleteTopicByDate(monthThreeshold)
    }
}
