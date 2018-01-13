package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.BookmarksMapper
import com.sedsoftware.yaptalker.data.parsed.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapBookmarksRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: BookmarksMapper,
    private val responseMapper: ServerResponseMapper
) : BookmarksRepository {

  companion object {
    private const val BOOKMARKS_ACT = "UserCP"
    private const val BOOKMARKS_CODE_LOAD = "10"
    private const val BOOKMARKS_CODE_ADD = "11"
    private const val BOOKMARKS_CODE_REMOVE = "12"
    private const val BOOKMARKS_TYPE = 1
  }

  override fun getBookmarks(): Observable<BaseEntity> =
      dataLoader
          .loadBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_LOAD
          )
          .map { parsedBookmarks -> dataMapper.transform(parsedBookmarks) }
          .flatMap { bookmarksList -> Observable.fromIterable(bookmarksList) }

  override fun requestBookmarkAdding(topicId: Int, startingPost: Int): Observable<BaseEntity> =
      dataLoader
          .addToBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_ADD,
              item = topicId,
              startPostNumber = startingPost,
              type = BOOKMARKS_TYPE)
          .map { response -> responseMapper.transform(response) }

  override fun requestBookmarkDeletion(bookmarkId: Int): Observable<BaseEntity> =
      dataLoader
          .removeFromBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_REMOVE,
              id = bookmarkId)
          .map { response -> responseMapper.transform(response) }
}
