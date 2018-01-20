package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Single

/**
 * Interface that represents a Repository for getting emoji list.
 */

interface EmojiRepository {

  fun getEmojiList(): Single<List<BaseEntity>>
}
