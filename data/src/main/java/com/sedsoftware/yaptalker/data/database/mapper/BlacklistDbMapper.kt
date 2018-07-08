package com.sedsoftware.yaptalker.data.database.mapper

import com.sedsoftware.yaptalker.data.database.model.BlacklistedTopicDbModel
import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import javax.inject.Inject

class BlacklistDbMapper @Inject constructor() {

    fun mapFromDb(from: BlacklistedTopicDbModel): BlacklistedTopic =
        BlacklistedTopic(
            topicName = from.topicName,
            topicId = from.topicId,
            dateAdded = from.dateAdded
        )

    fun mapToDb(from: BlacklistedTopic): BlacklistedTopicDbModel =
        BlacklistedTopicDbModel(
            topicName = from.topicName,
            topicId = from.topicId,
            dateAdded = from.dateAdded
        )
}
