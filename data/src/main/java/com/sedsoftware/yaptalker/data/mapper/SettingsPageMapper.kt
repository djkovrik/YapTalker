package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.SitePreferencesPageParsed
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import io.reactivex.functions.Function
import javax.inject.Inject

class SettingsPageMapper @Inject constructor() : Function<SitePreferencesPageParsed, SitePreferences> {

    companion object {
        private const val MESSAGES_PER_PAGE_DEFAULT = 25
        private const val TOPICS_PER_PAGE_DEFAULT = 30
    }

    override fun apply(from: SitePreferencesPageParsed): SitePreferences {

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
