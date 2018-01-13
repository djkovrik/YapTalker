package com.sedsoftware.yaptalker.domain.interactor.authorization

import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCase
import com.sedsoftware.yaptalker.domain.repository.SitePreferencesRepository
import io.reactivex.Completable
import javax.inject.Inject

class GetSiteUserPreferences @Inject constructor(
    private val siteSettingsRepository: SitePreferencesRepository,
    private val settings: Settings
) : CompletableUseCase {

  override fun execute(): Completable =
      siteSettingsRepository
          .getSitePreferences()
          .flatMapCompletable { sitePrefs ->
            sitePrefs as SitePreferences
            settings.saveMessagesPerPagePref(sitePrefs.messagesPerTopicPage)
            settings.saveTopicsPerPagePref(sitePrefs.topicsPerForumPage)
            Completable.complete()
          }
}
