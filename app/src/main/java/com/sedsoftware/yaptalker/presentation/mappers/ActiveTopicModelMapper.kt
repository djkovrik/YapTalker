package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ActiveTopic
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import com.sedsoftware.yaptalker.presentation.extensions.getLastDigits
import com.sedsoftware.yaptalker.presentation.mappers.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mappers.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.ActiveTopicModel
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import java.util.ArrayList
import javax.inject.Inject

/**
 * Mapper class used to transform active topics entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class ActiveTopicModelMapper @Inject constructor(
    private val textTransformer: TextTransformer,
    private val dateTransformer: DateTransformer) {

  fun transform(items: List<BaseEntity>): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList(items.size)

    items.forEach { item ->
      when (item) {
        is NavigationPanel -> result.add(NavigationPanelModel(
            currentPage = item.currentPage,
            totalPages = item.totalPages,
            navigationLabel = textTransformer.createNavigationLabel(item.currentPage, item.totalPages)
        ))
        is ActiveTopic -> result.add(ActiveTopicModel(
            title = textTransformer.createForumTopicTitle(item.isPinned, item.isClosed, item.title),
            link = item.link,
            topicId = item.link.getLastDigits(),
            isPinned = item.isPinned,
            isClosed = item.isClosed,
            forumTitle = item.forumTitle,
            forumLink = item.forumLink,
            forumId = item.forumLink.getLastDigits(),
            rating = item.rating,
            ratingText = item.rating.toString(),
            answers = textTransformer.createCommentsLabel(item.answers),
            lastPostDate = dateTransformer.transformDateToShortView(item.lastPostDate)
        ))
      }
    }

    return result
  }
}
