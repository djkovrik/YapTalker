package com.sedsoftware.yaptalker.data.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase

@Entity(tableName = YapTalkerDatabase.TAGS_BLACKLIST_TABLE)
data class BlacklistedTagDbModel(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0,
  @ColumnInfo(name = "tag_name")
  var tagName: String = "",
  @ColumnInfo(name = "tag_link")
  var tagLink: String = ""
)