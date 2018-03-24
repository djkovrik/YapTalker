package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Single

interface VersionInfoRepository {

  fun getActualVersionInfo(): Single<BaseEntity>
}
