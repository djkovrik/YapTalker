package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.EmojiListMapper
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapEmojiRepository @Inject constructor(
  private val dataLoader: YapLoader,
  private val dataMapper: EmojiListMapper,
  private val listMapper: ListToObservablesMapper
) : EmojiRepository {

  companion object {
    private const val EMOJI_ACT = "legends"
    private const val EMOJI_CODE = "emoticons"
  }

  override fun getEmojiList(): Observable<BaseEntity> =
    dataLoader
      .loadEmojiList(
        act = EMOJI_ACT,
        code = EMOJI_CODE
      )
      .map(dataMapper)
      .flatMapObservable(listMapper)
}
