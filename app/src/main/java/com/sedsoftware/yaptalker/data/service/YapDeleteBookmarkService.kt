package com.sedsoftware.yaptalker.data.service

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.DeleteBookmarkService
import io.reactivex.Observable
import javax.inject.Inject

class YapDeleteBookmarkService @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ServerResponseMapper
) : DeleteBookmarkService {

  companion object {
    private const val BOOKMARKS_ACT = "UserCP"
    private const val BOOKMARKS_CODE_REMOVE = "12"
  }

  override fun requestBookmarkDeletion(bookmarkId: Int): Observable<BaseEntity> =
      dataLoader
          .removeFromBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_REMOVE,
              id = bookmarkId)
          .map { response -> dataMapper.transform(response) }
}
