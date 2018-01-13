package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.GetUserProfile.Params
import com.sedsoftware.yaptalker.domain.repository.UserProfileRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetUserProfile @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      userProfileRepository
          .getUserProfile(params.userId)

  class Params(val userId: Int)
}
