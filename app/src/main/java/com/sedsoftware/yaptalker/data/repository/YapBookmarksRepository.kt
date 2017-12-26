package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.BookmarksMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapBookmarksRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: BookmarksMapper
) : BookmarksRepository {

  companion object {
    private const val BOOKMARKS_ACT = "UserCP"
    private const val BOOKMARKS_CODE_LOAD = "10"
  }

  override fun getBookmarks(): Observable<BaseEntity> =
      dataLoader
          .loadBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_LOAD
          )
          .map { parsedBookmarks -> dataMapper.transform(parsedBookmarks) }
          .flatMap { bookmarksList -> Observable.fromIterable(bookmarksList) }
}
