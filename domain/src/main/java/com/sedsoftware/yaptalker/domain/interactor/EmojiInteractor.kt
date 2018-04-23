package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import io.reactivex.Observable
import javax.inject.Inject

class EmojiInteractor @Inject constructor(
  private val emojiRepository: EmojiRepository
) {

  fun loadEmojiList(): Observable<BaseEntity> =
    emojiRepository
      .getEmojiList()
}
