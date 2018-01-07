package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.SitePreferencesPageParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import javax.inject.Inject

/**
 * Mapper class used to transform parsed settings page rom the data layer into BaseEntity in the domain layer.
 */
class SettingsPageMapper @Inject constructor() {

  fun transform(sitePreferences: SitePreferencesPageParsed): BaseEntity =
      SitePreferences(
          messagesPerTopicPage = sitePreferences.messagesPerTopicPage.toInt(),
          topicsPerForumPage = sitePreferences.topicsPerForumPage.toInt()
      )
}
