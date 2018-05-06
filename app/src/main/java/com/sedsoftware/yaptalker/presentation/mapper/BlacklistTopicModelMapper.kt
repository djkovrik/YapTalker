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
) : Function<List<BlacklistedTopic>, List<BlacklistedTopicModel>> {

  override fun apply(from: List<BlacklistedTopic>): List<BlacklistedTopicModel> =
    from.map { element ->
      BlacklistedTopicModel(
        topicName = element.topicName,
        topicId = element.topicId,
        dateAdded = element.dateAdded,
        dateAddedLabel = textTransformer.transformBlacklistDate(
          dateTransformer.transformLongToDateString(element.dateAdded.time)
        )
      )
    }
}
