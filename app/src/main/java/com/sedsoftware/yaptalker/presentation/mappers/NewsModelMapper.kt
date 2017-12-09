package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel

/**
 * Mapper class used to transform news entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class NewsModelMapper {

  fun transform(items: List<BaseEntity>): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList(items.size)

    items.forEach { item ->
      if (item is NewsItem) {
        result.add(NewsItemModel(
            title = item.title,
            link = item.link,
            rating = item.rating,
            description = item.description,
            images = item.images,
            videos = item.videos,
            videosRaw = item.videosRaw,
            author = item.author,
            authorLink = item.authorLink,
            date = item.date,
            forumName = item.forumName,
            forumLink = item.forumLink,
            comments = item.comments,
            cleanedDescription = item.description
        ))
      }
    }

    return result
  }
}