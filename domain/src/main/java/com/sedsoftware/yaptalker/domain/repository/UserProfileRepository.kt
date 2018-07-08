package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import io.reactivex.Single

interface UserProfileRepository {

    fun getUserProfile(userId: Int): Single<UserProfile>
}
