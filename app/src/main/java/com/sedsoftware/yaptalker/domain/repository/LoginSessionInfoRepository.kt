package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting user login session related data.
 */
interface LoginSessionInfoRepository {

  fun getLoginSessionInfo(): Observable<BaseEntity>
}
