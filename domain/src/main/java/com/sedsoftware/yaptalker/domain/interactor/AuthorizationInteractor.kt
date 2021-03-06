package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import com.sedsoftware.yaptalker.domain.repository.SitePreferencesRepository
import io.reactivex.Completable
import java.lang.IllegalArgumentException
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
                settings.saveMessagesPerPagePref(sitePrefs.messagesPerTopicPage)
                settings.saveTopicsPerPagePref(sitePrefs.topicsPerForumPage)
                Completable.complete()
            }

    fun sendSignInRequest(login: String, password: String, anonymously: Boolean): Completable =
        loginSessionRepository
            .requestSignIn(login, password, anonymously)

    fun sendSignInRequestNew(login: String, password: String): Completable =
        loginSessionRepository
            .requestSignInWithApi(login, password)

    fun tryToRestoreSession(): Completable =
        if (settings.getLogin().isNotEmpty() && settings.getPassword().isNotEmpty()) {
            sendSignInRequestNew(settings.getLogin(), settings.getPassword())
        } else {
            Completable.error(IllegalArgumentException("No saved user data"))
        }
}
