package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.executor.ExecutionThread
import com.sedsoftware.yaptalker.domain.executor.PostExecutionThread
import com.sedsoftware.yaptalker.domain.repository.LoginSessionInfoRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class GetLoginSessionInfo @Inject constructor(
    @Named("io") executionThread: ExecutionThread,
    @Named("ui") postExecutionThread: PostExecutionThread,
    private val sessionInfoRepository: LoginSessionInfoRepository
) : UseCase<BaseEntity, Unit>(executionThread, postExecutionThread) {

  override fun buildUseCaseObservable(params: Unit): Observable<BaseEntity> =
      sessionInfoRepository
          .getLoginSessionInfo()
}