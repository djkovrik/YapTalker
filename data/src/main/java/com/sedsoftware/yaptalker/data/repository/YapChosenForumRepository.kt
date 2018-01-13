package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ForumPageMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapChosenForumRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ForumPageMapper
) : ChosenForumRepository {

  override fun getChosenForum(forumId: Int, startPageNumber: Int, sortingMode: String): Observable<List<BaseEntity>> =
      dataLoader
          .loadForumPage(forumId, startPageNumber, sortingMode)
          .map { forumPageParsed -> dataMapper.transform(forumPageParsed) }
}
