package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents server response string in presentation layer.
 */
class ServerResponseModel(
  val text: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.SERVER_RESPONSE
}
