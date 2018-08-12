package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.data.extensions.getLastDigits
import com.sedsoftware.yaptalker.domain.entity.base.IncubatorItem
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.VideoTypeDetector
import com.sedsoftware.yaptalker.presentation.model.base.IncubatorItemModel
import io.reactivex.functions.Function
import javax.inject.Inject

class IncubatorModelMapper @Inject constructor(
    private val textTransformer: TextTransformer,
    private val dateTransformer: DateTransformer,
    private val videoTypeDetector: VideoTypeDetector
) : Function<List<IncubatorItem>, List<IncubatorItemModel>> {

    override fun apply(from: List<IncubatorItem>): List<IncubatorItemModel> =
        from.map { mapItem(it) }

    private fun mapItem(item: IncubatorItem): IncubatorItemModel =
        IncubatorItemModel(
            title = item.title,
            link = item.link,
            topicId = item.link.getLastDigits(),
            rating = textTransformer.transformRankToFormattedText(item.rating),
            images = item.images,
            videos = item.videos,
            videosRaw = item.videosRaw,
            videoTypes = item.videos.map { videoTypeDetector.detectVideoType(it) },
            author = item.author,
            authorLink = item.authorLink,
            date = dateTransformer.transformDateToShortView(item.date),
            forumName = textTransformer.transformNewsForumTitle(item.forumName),
            forumLink = item.forumLink,
            forumId = item.forumLink.getLastDigits(),
            comments = textTransformer.transformCommentsLabel(item.comments),
            cleanedDescription = textTransformer.transformHtmlToSpanned(item.cleanedDescription),
            isYapLink = item.link.contains("yaplakal") && !item.link.contains("/go/")
        )
}
