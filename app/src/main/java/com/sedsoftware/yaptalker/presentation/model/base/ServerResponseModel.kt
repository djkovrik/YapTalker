package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

/**
 * Class which represents server response string in presentation layer.
 */
class ServerResponseModel(
  val text: String
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.SERVER_RESPONSE
}
