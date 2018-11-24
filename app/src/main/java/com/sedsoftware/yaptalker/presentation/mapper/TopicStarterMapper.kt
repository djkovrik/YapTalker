package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostModel
import javax.inject.Inject

class TopicStarterMapper @Inject constructor() {

    fun updateAuthorId(from: DisplayedItemModel, givenAuthorId: Int): DisplayedItemModel =
        if (from is SinglePostModel) {
            from.apply { isTopicStarterHere = from.authorProfileId == givenAuthorId }
        } else {
            from
        }
}
