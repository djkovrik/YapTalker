package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.base.BlacklistedTopicModel
import io.reactivex.functions.Function
import javax.inject.Inject

class BlacklistTopicModelMapper @Inject constructor(
  private val textTransformer: TextTransformer,
  private val dateTransformer: DateTransformer
) : Function<BlacklistedTopic, BlacklistedTopicModel> {

  override fun apply(from: BlacklistedTopic): BlacklistedTopicModel =
    BlacklistedTopicModel(
      topicName = from.topicName,
      topicId = from.topicId,
      dateAdded = from.dateAdded,
      dateAddedLabel = textTransformer.transformBlacklistDate(
        dateTransformer.transformLongToDateString(from.dateAdded.time)
      )
    )
}
