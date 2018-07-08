package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import io.reactivex.Completable
import io.reactivex.Single

interface LoginSessionRepository {

    fun getLoginSessionInfo(): Single<LoginSessionInfo>

    fun requestSignIn(userLogin: String, userPassword: String, anonymously: Boolean): Completable

    fun requestSignOut(userKey: String): Completable
}
