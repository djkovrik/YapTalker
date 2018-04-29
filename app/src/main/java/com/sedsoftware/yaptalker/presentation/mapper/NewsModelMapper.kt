package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.presentation.extensions.getLastDigits
import com.sedsoftware.yaptalker.presentation.mapper.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import io.reactivex.functions.Function
import javax.inject.Inject

class NewsModelMapper @Inject constructor(
  private val textTransformer: TextTransformer,
  private val dateTransformer: DateTransformer
) : Function<NewsItem, NewsItemModel> {

  override fun apply(item: NewsItem): NewsItemModel =
    NewsItemModel(
      title = item.title,
      link = item.link,
      topicId = item.link.getLastDigits(),
      rating = textTransformer.transformRankToFormattedText(item.rating),
      images = item.images,
      videos = item.videos,
      videosRaw = item.videosRaw,
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
