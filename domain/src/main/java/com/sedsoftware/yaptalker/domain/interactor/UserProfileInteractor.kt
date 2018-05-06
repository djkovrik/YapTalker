package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import com.sedsoftware.yaptalker.domain.repository.UserProfileRepository
import io.reactivex.Single
import javax.inject.Inject

class UserProfileInteractor @Inject constructor(
  private val userProfileRepository: UserProfileRepository
) {

  fun getUserProfile(userId: Int): Single<UserProfile> =
    userProfileRepository
      .getUserProfile(userId)
}
