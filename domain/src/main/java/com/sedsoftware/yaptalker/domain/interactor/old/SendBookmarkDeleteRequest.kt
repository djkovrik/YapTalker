package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendBookmarkDeleteRequest.Params
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Observable
import javax.inject.Inject

class SendBookmarkDeleteRequest @Inject constructor(
    private val bookmarksRepository: BookmarksRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      bookmarksRepository
          .requestBookmarkDeletion(params.bookmarkId)

  class Params(val bookmarkId: Int)
}
