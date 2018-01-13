package com.sedsoftware.yaptalker.data.service

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.AddBookmarkService
import io.reactivex.Observable
import javax.inject.Inject

class YapAddBookmarkService @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ServerResponseMapper
) : AddBookmarkService {

  companion object {
    private const val BOOKMARKS_ACT = "UserCP"
    private const val BOOKMARKS_CODE_ADD = "11"
    private const val BOOKMARKS_TYPE = 1
  }

  override fun requestBookmarkAdding(topicId: Int, startingPost: Int): Observable<BaseEntity> =
      dataLoader
          .addToBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_ADD,
              item = topicId,
              startPostNumber = startingPost,
              type = BOOKMARKS_TYPE)
          .map { response -> dataMapper.transform(response) }
}
