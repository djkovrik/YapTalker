package com.sedsoftware.yaptalker.domain.interactor.authorization

import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Completable
import javax.inject.Inject

class SendSignInRequest @Inject constructor(
  private val loginSessionRepository: LoginSessionRepository
) : CompletableUseCaseWithParameter<SendSignInRequest.Params> {

  override fun execute(parameter: Params): Completable =
    loginSessionRepository
      .requestSignIn(parameter.login, parameter.password, parameter.anonymously)

  class Params(val login: String, val password: String, val anonymously: Boolean)
}
