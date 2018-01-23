package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.data.parsed.EmojiListParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform parsed emoji page from the data layer into BaseEntity list in the domain layer.
 */
class EmojiListMapper @Inject constructor() : Function<EmojiListParsed, List<BaseEntity>> {

  override fun apply(from: EmojiListParsed): List<BaseEntity> =
    from
      .emojis
      .map { emoji -> Emoji(emoji.code, emoji.link) }
}
