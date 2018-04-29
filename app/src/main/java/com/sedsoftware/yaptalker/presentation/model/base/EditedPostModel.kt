package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

/**
 * Class which represents edited post text in presentation layer.
 */
class EditedPostModel(
  val text: String
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.EDITED_POST_TEXT
}
