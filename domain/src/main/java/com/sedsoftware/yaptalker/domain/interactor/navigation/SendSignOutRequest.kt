package com.sedsoftware.yaptalker.domain.interactor.navigation

import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Completable
import javax.inject.Inject

class SendSignOutRequest @Inject constructor(
  private val loginSessionRepository: LoginSessionRepository,
  private val settings: Settings
) : CompletableUseCaseWithParameter<String> {

  companion object {
    private const val MESSAGES_PER_PAGE_DEFAULT = 25
    private const val TOPICS_PER_PAGE_DEFAULT = 30
  }

  override fun execute(parameter: String): Completable =
    loginSessionRepository
      .requestSignOut(userKey = parameter)
      .doOnComplete {
        settings.saveMessagesPerPagePref(MESSAGES_PER_PAGE_DEFAULT)
        settings.saveTopicsPerPagePref(TOPICS_PER_PAGE_DEFAULT)
        settings.clearCookieSid()
      }
}
