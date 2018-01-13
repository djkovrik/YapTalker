package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting user login session related data.
 */
interface LoginSessionRepository {

  fun getLoginSessionInfo(): Observable<BaseEntity>

  fun requestSignIn(userLogin: String, userPassword: String, anonymously: Boolean): Observable<BaseEntity>

  fun requestSignOut(userKey: String): Observable<BaseEntity>
}
