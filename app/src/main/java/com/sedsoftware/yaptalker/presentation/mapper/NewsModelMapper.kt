package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.data.extensions.getLastDigits
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.VideoTypeDetector
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import io.reactivex.functions.Function
import javax.inject.Inject

class NewsModelMapper @Inject constructor(
    private val textTransformer: TextTransformer,
    private val dateTransformer: DateTransformer,
    private val videoTypeDetector: VideoTypeDetector
) : Function<List<NewsItem>, List<NewsItemModel>> {

    override fun apply(from: List<NewsItem>): List<NewsItemModel> =
        from.map { mapItem(it) }

    private fun mapItem(item: NewsItem): NewsItemModel =
        NewsItemModel(
            title = item.title,
            link = item.link,
            topicId = item.id,
            rating = textTransformer.transformRankToFormattedText(item.rating),
            images = item.images,
            videos = item.videos,
            videosRaw = item.videosRaw,
            videosLinks = item.videosLinks,
            videoTypes = item.videos.map { videoTypeDetector.detectVideoType(it) },
            author = item.author,
            authorLink = item.authorLink,
            date = dateTransformer.transformDateToShortView(item.date),
            forumName = textTransformer.transformNewsForumTitle(item.forumName),
            forumLink = item.forumLink,
            forumId = item.forumLink.getLastDigits(),
            comments = textTransformer.transformCommentsLabel(item.comments),
            cleanedDescription = textTransformer.transformHtmlToSpanned(item.cleanedDescription),
            isYapLink = item.isYapLink
        )
}
