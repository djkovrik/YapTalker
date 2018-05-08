package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.model.base.EmojiModel
import io.reactivex.functions.Function
import javax.inject.Inject

class EmojiModelMapper @Inject constructor() : Function<Emoji, EmojiModel> {

  override fun apply(emoji: Emoji): EmojiModel =
    EmojiModel(emoji.code, emoji.link.validateUrl())
}
