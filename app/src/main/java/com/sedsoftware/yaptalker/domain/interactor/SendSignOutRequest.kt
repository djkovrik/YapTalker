package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.SignOutService
import io.reactivex.Observable
import javax.inject.Inject

class SendSignOutRequest @Inject constructor(
    private val signOutService: SignOutService
) : UseCase<BaseEntity, SendSignOutRequest.Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      signOutService
          .requestSignOut(params.userKey)

  class Params(val userKey: String)
}
