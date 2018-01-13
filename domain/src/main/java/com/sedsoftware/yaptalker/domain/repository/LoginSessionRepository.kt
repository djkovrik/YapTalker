package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that represents a Repository for getting user login session related data.
 */
interface LoginSessionRepository {

  fun getLoginSessionInfo(): Single<BaseEntity>

  fun requestSignIn(userLogin: String, userPassword: String, anonymously: Boolean): Single<BaseEntity>

  fun requestSignOut(userKey: String): Completable
}
