package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.UserProfileMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import com.sedsoftware.yaptalker.domain.repository.UserProfileRepository
import io.reactivex.Single
import javax.inject.Inject

class YapUserProfileRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: UserProfileMapper,
    private val schedulers: SchedulersProvider
) : UserProfileRepository {

    override fun getUserProfile(userId: Int): Single<UserProfile> =
        dataLoader
            .loadUserProfile(userId)
            .map(dataMapper)
            .subscribeOn(schedulers.io())
}
