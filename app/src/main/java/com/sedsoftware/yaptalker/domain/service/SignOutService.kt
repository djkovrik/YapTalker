package com.sedsoftware.yaptalker.domain.service

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents sign out service.
 */
interface SignOutService {

  fun requestSignOut(userKey: String): Observable<BaseEntity>
}
