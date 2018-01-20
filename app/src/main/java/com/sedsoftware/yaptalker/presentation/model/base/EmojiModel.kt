package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents emoji in presentation layer.
 */

class EmojiModel(
  val code: String,
  val link: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.EMOJI
}
