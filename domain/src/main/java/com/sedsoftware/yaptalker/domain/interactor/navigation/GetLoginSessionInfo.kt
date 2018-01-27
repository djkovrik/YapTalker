package com.sedsoftware.yaptalker.domain.interactor.navigation

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCase
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Single
import javax.inject.Inject

class GetLoginSessionInfo @Inject constructor(
  private val loginSessionRepository: LoginSessionRepository
) : SingleUseCase<BaseEntity> {

  override fun execute(): Single<BaseEntity> =
    loginSessionRepository
      .getLoginSessionInfo()
}
