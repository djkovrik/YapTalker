package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import com.sedsoftware.yaptalker.presentation.mappers.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import io.reactivex.functions.Function
import javax.inject.Inject

class VersionInfoMapper @Inject constructor(
  private val dateTransformer: DateTransformer,
  private val settings: Settings
) : Function<BaseEntity, YapEntity> {

  override fun apply(from: BaseEntity): YapEntity {
    from as VersionInfo

    return AppVersionInfoModel(
      versionCode = from.versionCode,
      versionName = from.versionName,
      downloadLink = from.downloadLink,
      lastUpdateCheckDate =
      dateTransformer.transformLongToDateString(settings.getLastUpdateCheckDate())
    )
  }
}
