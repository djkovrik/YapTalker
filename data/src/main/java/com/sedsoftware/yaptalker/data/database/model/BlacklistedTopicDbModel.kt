package com.sedsoftware.yaptalker.data.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import java.util.Date

@Entity(tableName = YapTalkerDatabase.TOPICS_BLACKLIST_TABLE)
data class BlacklistedTopicDbModel(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0L,
  @ColumnInfo(name = "topic_name")
  var topicName: String = "",
  @ColumnInfo(name = "topic_link")
  var topicLink: String = "",
  @ColumnInfo(name = "date_added")
  var dateAdded: Date = Date()
)
