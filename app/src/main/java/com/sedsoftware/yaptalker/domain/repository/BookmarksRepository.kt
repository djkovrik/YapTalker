package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting bookmarks related data.
 */
interface BookmarksRepository {

  fun getBookmarks(): Observable<BaseEntity>
}
