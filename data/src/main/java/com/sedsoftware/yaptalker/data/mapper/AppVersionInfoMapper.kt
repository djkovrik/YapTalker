package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.AppVersionInfo
import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import io.reactivex.functions.Function
import javax.inject.Inject

class AppVersionInfoMapper @Inject constructor() : Function<AppVersionInfo, VersionInfo> {

  override fun apply(from: AppVersionInfo): VersionInfo =
    VersionInfo(
      versionCode = from.versionCode,
      versionName = from.versionName,
      downloadLink = from.downloadLink
    )
}
