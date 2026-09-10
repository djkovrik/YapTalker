package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.UserProfileMapper
import com.sedsoftware.yaptalker.data.network.site.YapApi
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import com.sedsoftware.yaptalker.domain.repository.UserProfileRepository
import io.reactivex.Single
import javax.inject.Inject

class YapUserProfileRepository @Inject constructor(
    private val yapApi: YapApi,
    private val dataMapper: UserProfileMapper,
    private val schedulers: SchedulersProvider
) : UserProfileRepository {

    override fun getUserProfile(userId: Int): Single<UserProfile> =
        yapApi
            .loadUserProfile(userId)
            .map { result ->
                result.profile?.firstOrNull()
                    ?: throw IllegalStateException(result.message ?: "User profile is missing")
            }
            .map(dataMapper)
            .subscribeOn(schedulers.io())
}
