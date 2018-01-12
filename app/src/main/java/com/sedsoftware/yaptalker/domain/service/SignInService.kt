package com.sedsoftware.yaptalker.domain.service

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents sign in service.
 */
interface SignInService {

  fun requestSignIn(userLogin: String, userPassword: String, anonymously: Boolean): Observable<BaseEntity>
}
