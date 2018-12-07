package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.data.extensions.getLastDigits
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.SearchTopicItem
import com.sedsoftware.yaptalker.domain.entity.base.SearchTopicsPageInfo
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.SearchTopicItemModel
import com.sedsoftware.yaptalker.presentation.model.base.SearchTopicsPageInfoModel
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class SearchResultsModelMapper @Inject constructor(
    private val textTransformer: TextTransformer,
    private val dateTransformer: DateTransformer
) : Function<List<BaseEntity>, List<DisplayedItemModel>> {

    override fun apply(items: List<BaseEntity>): List<DisplayedItemModel> {
        val result: MutableList<DisplayedItemModel> = ArrayList(items.size)

        items.forEach { item ->
            when (item) {
                is SearchTopicItem -> {
                    result.add(
                        SearchTopicItemModel(
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
                        )
                    )
                }

                is SearchTopicsPageInfo -> {
                    result.add(
                        SearchTopicsPageInfoModel(
                            hasNextPage = item.hasNextPage,
                            searchId = item.searchId
                        )
                    )
                }
            }
        }

        return result
    }
}
