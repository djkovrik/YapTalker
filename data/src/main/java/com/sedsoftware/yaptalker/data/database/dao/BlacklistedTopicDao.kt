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
  abstract fun insertTopic(tag: BlacklistedTopicDbModel): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertTopics(tags: List<BlacklistedTopicDbModel>): List<Long>

  @Query("SELECT * FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE} WHERE topic_name LIKE :name")
  abstract fun getTopicByName(name: String): Single<BlacklistedTopicDbModel>

  @Query("SELECT * FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}  WHERE topic_link LIKE :link")
  abstract fun getTopicByLink(link: String): Single<BlacklistedTopicDbModel>

  @Query("SELECT * FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}")
  abstract fun getAllTopics(): Single<List<BlacklistedTopicDbModel>>

  @Query("DELETE FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}  WHERE topic_name LIKE :name")
  abstract fun deleteTopicByName(name: String): Int

  @Query("DELETE FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}  WHERE topic_link LIKE :link")
  abstract fun deleteTopicByLink(link: String): Int

  @Query("DELETE FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}")
  abstract fun deleteAllTopics()
}
