package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import java.util.Date

data class BlacklistedTopicModel(
    val topicName: String,
    val topicId: Int,
    val dateAdded: Date,
    val dateAddedLabel: String
) : DisplayedItemModel {

    override fun getEntityType(): Int = DisplayedItemType.BLACKLISTED_TOPIC
}
