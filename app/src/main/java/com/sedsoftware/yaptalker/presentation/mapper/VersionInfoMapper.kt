package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import io.reactivex.functions.Function
import javax.inject.Inject

class VersionInfoMapper @Inject constructor(
) : Function<VersionInfo, AppVersionInfoModel> {

  override fun apply(from: VersionInfo): AppVersionInfoModel =
    AppVersionInfoModel(
      versionCode = from.versionCode,
      versionName = from.versionName,
      downloadLink = from.downloadLink
    )
}
