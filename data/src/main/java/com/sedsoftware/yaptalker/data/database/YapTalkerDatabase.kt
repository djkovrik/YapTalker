package com.sedsoftware.yaptalker.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.sedsoftware.yaptalker.data.database.converter.DateConverter
import com.sedsoftware.yaptalker.data.database.dao.BlacklistedTopicDao
import com.sedsoftware.yaptalker.data.database.model.BlacklistedTopicDbModel

@Database(entities = [(BlacklistedTopicDbModel::class)], version = 2)
@TypeConverters(DateConverter::class)
abstract class YapTalkerDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "yaptalker_db"
        const val TOPICS_BLACKLIST_TABLE = "blacklist_topics"
    }

    abstract fun getTopicsDao(): BlacklistedTopicDao
}
