package com.sedsoftware.yaptalker.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.database.model.BlacklistedTopicDbModel
import io.reactivex.Single

@Dao
abstract class BlacklistedTopicDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertTopic(topic: BlacklistedTopicDbModel): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertTopics(topics: List<BlacklistedTopicDbModel>): List<Long>

  @Query("SELECT * FROM ${YapTalkerDatabase.TOPICS_BLACKLIST_TABLE} WHERE topic_name LIKE :name")
  abstract fun getTopicByName(name: String): Single<BlacklistedTopicDbModel>

  @Query("SELECT * FROM ${YapTalkerDatabase.TOPICS_BLACKLIST_TABLE}")
  abstract fun getAllTopics(): Single<List<BlacklistedTopicDbModel>>

  @Query("SELECT * FROM ${YapTalkerDatabase.TOPICS_BLACKLIST_TABLE}")
  abstract fun getAllTopicIds(): List<BlacklistedTopicDbModel>

  @Query("DELETE FROM ${YapTalkerDatabase.TOPICS_BLACKLIST_TABLE}  WHERE topic_name LIKE :name")
  abstract fun deleteTopicByName(name: String): Int

  @Query("DELETE FROM ${YapTalkerDatabase.TOPICS_BLACKLIST_TABLE}  WHERE topic_id LIKE :id")
  abstract fun deleteTopicById(id: Int): Int

  @Query("DELETE FROM ${YapTalkerDatabase.TOPICS_BLACKLIST_TABLE}  WHERE date_added <= :threshold")
  abstract fun deleteTopicByDate(threshold: Long): Int

  @Query("DELETE FROM ${YapTalkerDatabase.TOPICS_BLACKLIST_TABLE}")
  abstract fun deleteAllTopics()

  fun getBlacklistedTopicIds(): Single<Set<Int>> =
    getAllTopics()
      .map { topics -> topics.map { it.topicId } }
      .map { ids -> ids.toSet() }
}
