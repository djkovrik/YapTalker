package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendSignOutRequest.Params
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class SendSignOutRequest @Inject constructor(
    private val loginSessionRepository: LoginSessionRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      loginSessionRepository
          .requestSignOut(params.userKey)

  class Params(val userKey: String)
}
