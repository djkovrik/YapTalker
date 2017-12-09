package com.sedsoftware.yaptalker.model.mappers

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.base.NewsItemModel

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