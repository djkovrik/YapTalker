package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.data.extensions.getLastDigits
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ActiveTopic
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.ActiveTopicModel
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class ActiveTopicModelMapper @Inject constructor(
    private val textTransformer: TextTransformer,
    private val dateTransformer: DateTransformer
) : Function<List<BaseEntity>, List<DisplayedItemModel>> {

    override fun apply(items: List<BaseEntity>): List<DisplayedItemModel> {
        val result: MutableList<DisplayedItemModel> = ArrayList(items.size)

        items.forEach { item ->
            when (item) {
                is NavigationPanel -> result.add(
                    NavigationPanelModel(
                        currentPage = item.currentPage,
                        totalPages = item.totalPages,
                        navigationLabel = textTransformer.createNavigationLabel(item.currentPage, item.totalPages)
                    )
                )
                is ActiveTopic -> result.add(
                    ActiveTopicModel(
                        title = textTransformer.createForumTopicTitle(item.isPinned, item.isClosed, item.title),
                        link = item.link,
                        topicId = item.id,
                        isPinned = item.isPinned,
                        isClosed = item.isClosed,
                        forumTitle = item.forumTitle,
                        forumLink = item.forumLink,
                        forumId = item.forumLink.getLastDigits(),
                        rating = item.rating,
                        ratingText = item.rating.toString(),
                        answers = textTransformer.transformCommentsLabelShort(item.answers),
                        lastPostDate = dateTransformer.transformDateToShortView(item.lastPostDate)
                    )
                )
            }
        }

        return result
    }
}
