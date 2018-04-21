package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LoginSessionInteractor @Inject constructor(
  private val loginSessionRepository: LoginSessionRepository,
  private val settings: Settings
) {

  companion object {
    private const val MESSAGES_PER_PAGE_DEFAULT = 25
    private const val TOPICS_PER_PAGE_DEFAULT = 30
  }

  fun getLoginSessionInfo(): Single<BaseEntity> =
    loginSessionRepository
      .getLoginSessionInfo()

  fun sendSignOutRequest(userKey: String): Completable =
    loginSessionRepository
      .requestSignOut(userKey)
      .doOnComplete {
        settings.saveMessagesPerPagePref(MESSAGES_PER_PAGE_DEFAULT)
        settings.saveTopicsPerPagePref(TOPICS_PER_PAGE_DEFAULT)
      }
}
