package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.presentation.mappers.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import javax.inject.Inject

/**
 * Mapper class used to transform news entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class NewsModelMapper @Inject constructor(private val textTransformer: TextTransformer) {

  fun transform(item: BaseEntity): YapEntity {

    item as NewsItem

    return NewsItemModel(
        title = item.title,
        link = item.link,
        rating = item.rating,
        description = textTransformer.transformHtmlToSpanned(item.description),
        images = item.images,
        videos = item.videos,
        videosRaw = item.videosRaw,
        author = item.author,
        authorLink = item.authorLink,
        date = item.date,
        forumName = item.forumName,
        forumLink = item.forumLink,
        comments = item.comments,
        cleanedDescription = textTransformer.transformHtmlToSpanned(item.cleanedDescription)
    )
  }
}
