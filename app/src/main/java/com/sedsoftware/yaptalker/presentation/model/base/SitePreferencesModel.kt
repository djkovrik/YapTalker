package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

/**
 * Class which represents user site preferences in presentation layer.
 */
class SitePreferencesModel(
  val messagesPerTopicPage: Int,
  val topicsPerForumPage: Int
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.USER_SITE_PREFERENCES
}
