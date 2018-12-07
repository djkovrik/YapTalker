package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.Emoji
import io.reactivex.Observable

interface EmojiRepository {
    fun getEmojiList(): Observable<Emoji>
}
