package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.SitePreferencesModel
import javax.inject.Inject

/**
 * Mapper class used to transform user site settings entity from the domain layer into YapEntity
 * in the presentation layer.
 */
class SitePreferencesModelMapper @Inject constructor() {

  companion object {
    private const val MESSAGES_PER_PAGE_DEFAULT = 25
    private const val TOPICS_PER_PAGE_DEFAULT = 30
  }

  fun transform(item: BaseEntity): YapEntity {

    item as SitePreferences

    val messages = if (item.messagesPerTopicPage == -1)
      MESSAGES_PER_PAGE_DEFAULT
    else
      item.messagesPerTopicPage

    val topics = if (item.topicsPerForumPage == -1)
      TOPICS_PER_PAGE_DEFAULT
    else
      item.topicsPerForumPage

    return SitePreferencesModel(
        messagesPerTopicPage = messages,
        topicsPerForumPage = topics
    )
  }
}
