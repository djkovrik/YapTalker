package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.EmojiListParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import javax.inject.Inject

/**
 * Mapper class used to transform parsed emoji page from the data layer into BaseEntity list in the domain layer.
 */
class EmojiListMapper @Inject constructor() {

  fun transform(emojiList: EmojiListParsed): List<BaseEntity> =
    emojiList
      .emojis
      .map { emoji -> Emoji(emoji.code, emoji.link) }
}
