package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.EmojiModel
import javax.inject.Inject

class EmojiModelMapper @Inject constructor() {

  fun transform(emoji: BaseEntity): YapEntity {

    emoji as Emoji

    return EmojiModel(emoji.code, emoji.link.validateUrl())
  }
}
