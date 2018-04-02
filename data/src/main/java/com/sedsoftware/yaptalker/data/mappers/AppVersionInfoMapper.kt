package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.data.parsed.AppVersionInfo
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import io.reactivex.functions.Function
import javax.inject.Inject

class AppVersionInfoMapper @Inject constructor() : Function<AppVersionInfo, BaseEntity> {

  override fun apply(from: AppVersionInfo): BaseEntity =
    VersionInfo(
      versionCode = from.versionCode,
      versionName = from.versionName,
      downloadLink = from.downloadLink
    )
}
