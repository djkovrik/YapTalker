package com.sedsoftware.yaptalker.domain.interactor.userprofile

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.UserProfileRepository
import io.reactivex.Single
import javax.inject.Inject

class GetUserProfile @Inject constructor(
  private val userProfileRepository: UserProfileRepository
) : SingleUseCaseWithParameter<GetUserProfile.Params, BaseEntity> {

  override fun execute(parameter: Params): Single<BaseEntity> =
    userProfileRepository
      .getUserProfile(parameter.userId)

  class Params(val userId: Int)
}
