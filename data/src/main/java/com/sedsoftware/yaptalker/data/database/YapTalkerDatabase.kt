package com.sedsoftware.yaptalker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sedsoftware.yaptalker.data.database.converter.DateConverter
import com.sedsoftware.yaptalker.data.database.dao.BlacklistedTagDao
import com.sedsoftware.yaptalker.data.database.dao.BlacklistedTopicDao
import com.sedsoftware.yaptalker.data.database.model.BlacklistedTagDbModel
import com.sedsoftware.yaptalker.data.database.model.BlacklistedTopicDbModel

@Database(
    entities = [
        (BlacklistedTagDbModel::class), (BlacklistedTopicDbModel::class)
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class YapTalkerDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "yaptalker_db"
        const val TAGS_BLACKLIST_TABLE = "blacklist_tags"
        const val TOPICS_BLACKLIST_TABLE = "blacklist_topics"
    }

    abstract fun getTagsDao(): BlacklistedTagDao
    abstract fun getTopicsDao(): BlacklistedTopicDao
}
