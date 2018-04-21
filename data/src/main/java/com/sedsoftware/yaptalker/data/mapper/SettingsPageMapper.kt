package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.SitePreferencesPageParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform parsed settings page rom the data layer into BaseEntity in the domain layer.
 */
class SettingsPageMapper @Inject constructor() : Function<SitePreferencesPageParsed, BaseEntity> {

  companion object {
    private const val MESSAGES_PER_PAGE_DEFAULT = 25
    private const val TOPICS_PER_PAGE_DEFAULT = 30
  }

  override fun apply(from: SitePreferencesPageParsed): BaseEntity {

    val messages = from.messagesPerTopicPage.toInt()
    val topics = from.topicsPerForumPage.toInt()

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
