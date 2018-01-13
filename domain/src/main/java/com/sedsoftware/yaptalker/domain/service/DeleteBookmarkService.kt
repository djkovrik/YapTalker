package com.sedsoftware.yaptalker.domain.service

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents bookmark deletion service.
 */
interface DeleteBookmarkService {

  fun requestBookmarkDeletion(bookmarkId: Int): Observable<BaseEntity>
}
