package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.executor.ExecutionThread
import com.sedsoftware.yaptalker.domain.executor.PostExecutionThread
import com.sedsoftware.yaptalker.domain.service.SignOutService
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class SendSignOutRequest @Inject constructor(
    @Named("io") executionThread: ExecutionThread,
    @Named("ui") postExecutionThread: PostExecutionThread,
    private val signOutService: SignOutService
) : UseCase<BaseEntity, SendSignOutRequest.Params>(executionThread, postExecutionThread) {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      signOutService
          .requestSignOut(params.userKey)

  class Params(val userKey: String)
}
