package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.LoginSessionInfoRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetLoginSessionInfo @Inject constructor(
    private val sessionInfoRepository: LoginSessionInfoRepository
) : UseCase<BaseEntity, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<BaseEntity> =
      sessionInfoRepository
          .getLoginSessionInfo()
}
