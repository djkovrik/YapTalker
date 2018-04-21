package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Single

/**
 * Interface that represents a Repository for getting version info related data.
 */
interface VersionInfoRepository {

  fun getRemoteVersionInfo(): Single<BaseEntity>

  fun getInstalledVersionInfo(): Single<BaseEntity>
}
