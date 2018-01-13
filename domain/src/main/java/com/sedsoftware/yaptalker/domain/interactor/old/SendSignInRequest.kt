package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendSignInRequest.Params
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class SendSignInRequest @Inject constructor(
    private val loginSessionRepository: LoginSessionRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      loginSessionRepository
          .requestSignIn(params.login, params.password, params.anonymously)

  class Params(val login: String, val password: String, val anonymously: Boolean)
}
