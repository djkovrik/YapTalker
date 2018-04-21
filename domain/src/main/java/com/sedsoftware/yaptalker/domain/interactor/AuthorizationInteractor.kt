package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import com.sedsoftware.yaptalker.domain.repository.SitePreferencesRepository
import io.reactivex.Completable
import javax.inject.Inject

class AuthorizationInteractor @Inject constructor(
  private val loginSessionRepository: LoginSessionRepository,
  private val siteSettingsRepository: SitePreferencesRepository,
  private val settings: Settings
) {

  fun getSiteUserPreferences(): Completable =
    siteSettingsRepository
      .getSitePreferences()
      .flatMapCompletable { sitePrefs ->
        sitePrefs as SitePreferences
        settings.saveMessagesPerPagePref(sitePrefs.messagesPerTopicPage)
        settings.saveTopicsPerPagePref(sitePrefs.topicsPerForumPage)
        Completable.complete()
      }

  fun sendSignInRequest(login: String, password: String, anonymously: Boolean): Completable =
    loginSessionRepository
      .requestSignIn(login, password, anonymously)
}
