package com.sedsoftware.yaptalker.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import java.util.Date

@Entity(tableName = YapTalkerDatabase.TOPICS_BLACKLIST_TABLE)
data class BlacklistedTopicDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "topic_name")
    var topicName: String = "",
    @ColumnInfo(name = "topic_id")
    var topicId: Int = 0,
    @ColumnInfo(name = "date_added")
    var dateAdded: Date = Date()
)
