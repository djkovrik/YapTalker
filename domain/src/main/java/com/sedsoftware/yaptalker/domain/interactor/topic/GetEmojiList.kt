package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.UseCase
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetEmojiList @Inject constructor(
  private val emojiRepository: EmojiRepository
) : UseCase<BaseEntity> {

  override fun execute(): Observable<BaseEntity> =
    emojiRepository
      .getEmojiList()
}
