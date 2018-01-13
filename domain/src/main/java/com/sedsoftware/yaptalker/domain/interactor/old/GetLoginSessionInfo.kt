package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.LoginSessionInfoRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetLoginSessionInfo @Inject constructor(
    private val sessionInfoRepository: LoginSessionInfoRepository
) : UseCaseOld<BaseEntity, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<BaseEntity> =
      sessionInfoRepository
          .getLoginSessionInfo()
}
