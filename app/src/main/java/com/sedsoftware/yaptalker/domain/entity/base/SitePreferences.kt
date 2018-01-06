package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents site user settings in domain layer.
 */
class SitePreferences(
    val messagesPerTopicPage: Int,
    val topicsPerForumPage: Int
): BaseEntity
