package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import com.sedsoftware.yaptalker.domain.entity.base.SinglePost
import com.sedsoftware.yaptalker.domain.entity.base.TopicInfoBlock
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostGalleryImageModel
import com.sedsoftware.yaptalker.presentation.model.base.TopicInfoBlockModel
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class TopicGalleryModelMapper @Inject constructor(
  private val textTransformer: TextTransformer
) : Function<List<BaseEntity>, List<YapEntity>> {

  override fun apply(items: List<BaseEntity>): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList()
    val imagesList: MutableList<String> = ArrayList()

    items.forEach { item ->
      when (item) {
        is TopicInfoBlock -> result.add(
          TopicInfoBlockModel(
            topicTitle = item.topicTitle,
            isClosed = item.isClosed,
            authKey = item.authKey,
            topicRating = item.topicRating,
            topicRatingPlusAvailable = item.topicRatingPlusAvailable,
            topicRatingMinusAvailable = item.topicRatingMinusAvailable,
            topicRatingPlusClicked = item.topicRatingPlusClicked,
            topicRatingMinusClicked = item.topicRatingMinusClicked,
            topicRatingTargetId = item.topicRatingTargetId
          )
        )

        is NavigationPanel -> {
          result.add(
            NavigationPanelModel(
              currentPage = item.currentPage,
              totalPages = item.totalPages,
              navigationLabel = textTransformer.createNavigationLabel(item.currentPage, item.totalPages)
            )
          )
        }

        is SinglePost -> imagesList.addAll(item.postContentParsed.images)
      }
    }

    imagesList.forEachIndexed { index, imageUrl ->
      result.add(
        SinglePostGalleryImageModel(
          url = imageUrl,
          showLoadMore = index == imagesList.lastIndex
        )
      )
    }

    return result
  }
}
