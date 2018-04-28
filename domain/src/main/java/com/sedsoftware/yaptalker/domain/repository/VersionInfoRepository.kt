package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import io.reactivex.Single

interface VersionInfoRepository {

  fun getRemoteVersionInfo(): Single<VersionInfo>

  fun getInstalledVersionInfo(): Single<VersionInfo>
}
