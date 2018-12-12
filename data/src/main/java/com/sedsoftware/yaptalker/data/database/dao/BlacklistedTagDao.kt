package com.sedsoftware.yaptalker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.database.model.BlacklistedTagDbModel
import io.reactivex.Single

@Dao
abstract class BlacklistedTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTag(tag: BlacklistedTagDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTags(tags: List<BlacklistedTagDbModel>): List<Long>

    @Query("SELECT * FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE} WHERE tag_name LIKE :name")
    abstract fun getTagByName(name: String): Single<BlacklistedTagDbModel>

    @Query("SELECT * FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}  WHERE tag_link LIKE :link")
    abstract fun getTagByLink(link: String): Single<BlacklistedTagDbModel>

    @Query("SELECT * FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}")
    abstract fun getAllTags(): Single<List<BlacklistedTagDbModel>>

    @Query("DELETE FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}  WHERE tag_name LIKE :name")
    abstract fun deleteTagByName(name: String): Int

    @Query("DELETE FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}  WHERE tag_link LIKE :link")
    abstract fun deleteTagByLink(link: String): Int

    @Query("DELETE FROM ${YapTalkerDatabase.TAGS_BLACKLIST_TABLE}")
    abstract fun deleteAllTags()
}
