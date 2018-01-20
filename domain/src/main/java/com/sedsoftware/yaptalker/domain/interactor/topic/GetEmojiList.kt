package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCase
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import io.reactivex.Single
import javax.inject.Inject

class GetEmojiList @Inject constructor(
  private val emojiRepository: EmojiRepository
) : SingleUseCase<List<BaseEntity>> {

  override fun execute(): Single<List<BaseEntity>> =
    emojiRepository
      .getEmojiList()
}
