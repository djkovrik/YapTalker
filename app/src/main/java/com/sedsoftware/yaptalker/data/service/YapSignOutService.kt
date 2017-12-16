package com.sedsoftware.yaptalker.data.service

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.SignOutService
import io.reactivex.Observable
import javax.inject.Inject

class YapSignOutService @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ServerResponseMapper
) : SignOutService {

  override fun requestSignOut(userKey: String): Observable<BaseEntity> =
      dataLoader
          .signOut(userKey)
          .map { response -> dataMapper.transform(response) }
}
