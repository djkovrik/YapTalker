package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents app version info in presentation layer.
 */
class AppVersionInfoModel(
  val versionCode: Int,
  val versionName: String,
  val downloadLink: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.VERSION_INFO
}
