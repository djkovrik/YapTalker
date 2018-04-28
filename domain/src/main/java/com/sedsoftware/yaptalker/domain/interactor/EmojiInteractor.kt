package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import io.reactivex.Observable
import javax.inject.Inject

class EmojiInteractor @Inject constructor(
  private val emojiRepository: EmojiRepository
) {

  fun loadEmojiList(): Observable<Emoji> =
    emojiRepository
      .getEmojiList()
}
