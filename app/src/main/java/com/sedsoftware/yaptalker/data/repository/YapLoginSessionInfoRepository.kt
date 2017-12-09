package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.YapLoader
import com.sedsoftware.yaptalker.data.parsing.mappers.LoginSessionInfoMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.LoginSessionInfoRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapLoginSessionInfoRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: LoginSessionInfoMapper
) : LoginSessionInfoRepository {

  override fun getLoginSessionInfo(): Observable<BaseEntity> =
      dataLoader
          .loadAuthorizedUserInfo()
          .map { info -> dataMapper.transform(info) }
}
