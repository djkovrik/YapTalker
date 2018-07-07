package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.EmojiListMapper
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapEmojiRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: EmojiListMapper,
    private val listMapper: ListToObservablesMapper<Emoji>
) : EmojiRepository {

    companion object {
        private const val EMOJI_ACT = "legends"
        private const val EMOJI_CODE = "emoticons"
    }

    override fun getEmojiList(): Observable<Emoji> =
        dataLoader
            .loadEmojiList(
                act = EMOJI_ACT,
                code = EMOJI_CODE
            )
            .map(dataMapper)
            .flatMapObservable(listMapper)
}
