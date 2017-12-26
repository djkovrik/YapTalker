package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.UserProfileMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.UserProfileRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapUserProfileRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: UserProfileMapper
) : UserProfileRepository {

  override fun getUserProfile(userId: Int): Observable<BaseEntity> =
      dataLoader
          .loadUserProfile(userId)
          .map { parsedUserProfile -> dataMapper.transform(parsedUserProfile) }
}
