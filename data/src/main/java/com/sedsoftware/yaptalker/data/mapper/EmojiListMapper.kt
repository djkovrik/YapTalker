package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.EmojiListParsed
import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import io.reactivex.functions.Function
import javax.inject.Inject

class EmojiListMapper @Inject constructor() : Function<EmojiListParsed, List<Emoji>> {

  override fun apply(from: EmojiListParsed): List<Emoji> =
    from.emojis
      .map { emoji -> Emoji(emoji.code, emoji.link) }
}
