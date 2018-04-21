package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import io.reactivex.functions.Function
import javax.inject.Inject

class VersionInfoMapper @Inject constructor(
) : Function<BaseEntity, AppVersionInfoModel> {

  override fun apply(from: BaseEntity): AppVersionInfoModel {

    from as VersionInfo

    return AppVersionInfoModel(
      versionCode = from.versionCode,
      versionName = from.versionName,
      downloadLink = from.downloadLink
    )
  }
}
