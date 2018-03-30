package com.sedsoftware.yaptalker.presentation.mappers

import android.content.Context
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.mappers.util.DateTransformer
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import io.reactivex.functions.Function
import java.util.*
import javax.inject.Inject

class VersionInfoMapper @Inject constructor(
  private val dateTransformer: DateTransformer,
  private val settings: Settings,
  private val context: Context
) : Function<BaseEntity, AppVersionInfoModel> {

  override fun apply(from: BaseEntity): AppVersionInfoModel {

    from as VersionInfo

    return AppVersionInfoModel(
      versionCode = from.versionCode,
      versionName = String.format(
        Locale.getDefault(),
        "%s %s",
        context.stringRes(R.string.updater_info_installed_version),
        from.versionName
      ),
      downloadLink = from.downloadLink,
      lastUpdateCheckDate = String.format(
        Locale.getDefault(),
        context.stringRes(R.string.updater_info_last_update_check),
        dateTransformer.transformLongToDateString(settings.getLastUpdateCheckDate())
      )
    )
  }
}
