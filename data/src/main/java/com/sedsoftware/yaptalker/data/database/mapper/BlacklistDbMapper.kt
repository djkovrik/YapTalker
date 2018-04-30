package com.sedsoftware.yaptalker.data.database.mapper

import com.sedsoftware.yaptalker.data.database.model.BlacklistedTagDbModel
import com.sedsoftware.yaptalker.data.database.model.BlacklistedTopicDbModel
import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTag
import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import javax.inject.Inject

class BlacklistDbMapper @Inject constructor() {

  fun mapFromDb(from: BlacklistedTagDbModel): BlacklistedTag =
    BlacklistedTag(
      tagName = from.tagName,
      tagLink = from.tagLink
    )

  fun mapFromDb(from: BlacklistedTopicDbModel): BlacklistedTopic =
    BlacklistedTopic(
      topicName = from.topicName,
      topicLink = from.topicLink,
      dateAdded = from.dateAdded
    )

  fun mapToDb(from: BlacklistedTag): BlacklistedTagDbModel =
    BlacklistedTagDbModel(
      tagName = from.tagName,
      tagLink = from.tagLink
    )

  fun mapToDb(from: BlacklistedTopic): BlacklistedTopicDbModel =
    BlacklistedTopicDbModel(
      topicName = from.topicName,
      topicLink = from.topicLink,
      dateAdded = from.dateAdded
    )
}
