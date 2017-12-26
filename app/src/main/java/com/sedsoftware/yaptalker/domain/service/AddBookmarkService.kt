package com.sedsoftware.yaptalker.domain.service

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents bookmark adding service.
 */
interface AddBookmarkService {

  fun requestBookmarkAdding(topicId: Int, startingPost: Int): Observable<BaseEntity>
}
