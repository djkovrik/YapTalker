package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.Forum
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel
import io.reactivex.functions.Function
import javax.inject.Inject

class ForumsListModelMapper @Inject constructor(
    private val dateTransformer: DateTransformer
) : Function<Forum, ForumModel> {

    override fun apply(item: Forum): ForumModel =
        ForumModel(
            title = item.title,
            forumId = item.forumId,
            lastTopicTitle = item.lastTopicTitle,
            lastTopicAuthor = item.lastTopicAuthor,
            date = dateTransformer.transformDateToShortView(item.date)
        )
}
