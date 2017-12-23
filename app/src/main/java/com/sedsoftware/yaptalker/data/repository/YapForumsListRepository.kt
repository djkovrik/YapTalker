package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ForumsListMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapForumsListRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ForumsListMapper
) : ForumsListRepository {

  override fun getMainForumsList(): Observable<BaseEntity> =
      dataLoader
          .loadForumsList()
          .map { parsedList -> dataMapper.transform(parsedList) }
          .flatMap { forumsList -> Observable.fromIterable(forumsList) }
}
