package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ForumInfoBlock
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import com.sedsoftware.yaptalker.domain.entity.base.Topic
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.ForumInfoBlockModel
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.sedsoftware.yaptalker.presentation.model.base.TopicModel
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class ForumModelMapper @Inject constructor(
  private val textTransformer: TextTransformer,
  private val dateTransformer: DateTransformer
) : Function<List<BaseEntity>, List<DisplayedItemModel>> {

  override fun apply(items: List<BaseEntity>): List<DisplayedItemModel> {

    val result: MutableList<DisplayedItemModel> = ArrayList(items.size)

    items.forEach { item ->
      when (item) {
        is ForumInfoBlock -> result.add(
          ForumInfoBlockModel(
            forumTitle = item.forumTitle,
            forumId = item.forumId
          )
        )
        is NavigationPanel -> result.add(
          NavigationPanelModel(
            currentPage = item.currentPage,
            totalPages = item.totalPages,
            navigationLabel = textTransformer.createNavigationLabel(item.currentPage, item.totalPages)
          )
        )
        is Topic -> result.add(
          TopicModel(
            title = textTransformer.createForumTopicTitle(item.isPinned, item.isClosed, item.title),
            link = item.link,
            id = item.id,
            isPinned = item.isPinned,
            isClosed = item.isClosed,
            author = item.author,
            authorLink = item.authorLink,
            rating = item.rating,
            ratingText = item.rating.toString(),
            answers = textTransformer.transformCommentsLabelShort(item.answers),
            lastPostDate = dateTransformer.transformDateToShortView(item.lastPostDate),
            lastPostAuthor = item.lastPostAuthor
          )
        )
      }
    }

    return result
  }
}
