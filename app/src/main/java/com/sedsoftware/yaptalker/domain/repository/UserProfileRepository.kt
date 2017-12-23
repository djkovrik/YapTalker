package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting user profile related data.
 */
interface UserProfileRepository {

  fun getUserProfile(userId: Int) : Observable<BaseEntity>
}
