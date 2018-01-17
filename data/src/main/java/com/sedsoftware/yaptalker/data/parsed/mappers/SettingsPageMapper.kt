package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.SitePreferencesPageParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import javax.inject.Inject

/**
 * Mapper class used to transform parsed settings page rom the data layer into BaseEntity in the domain layer.
 */
class SettingsPageMapper @Inject constructor() {

  companion object {
    private const val MESSAGES_PER_PAGE_DEFAULT = 25
    private const val TOPICS_PER_PAGE_DEFAULT = 30
  }

  fun transform(sitePreferences: SitePreferencesPageParsed): BaseEntity {

    val messages = sitePreferences.messagesPerTopicPage.toInt()
    val topics = sitePreferences.topicsPerForumPage.toInt()

    val mappedMessages = if (messages == -1)
      MESSAGES_PER_PAGE_DEFAULT
    else
      messages

    val mappedTopics = if (topics == -1)
      TOPICS_PER_PAGE_DEFAULT
    else
      topics

    return SitePreferences(
      messagesPerTopicPage = mappedMessages,
      topicsPerForumPage = mappedTopics
    )
  }
}
