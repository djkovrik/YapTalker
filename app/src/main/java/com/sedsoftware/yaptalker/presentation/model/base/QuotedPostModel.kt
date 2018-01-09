package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents quoted post text in presentation layer.
 */
class QuotedPostModel(
    val text: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.QUOTED_POST_TEXT
}
