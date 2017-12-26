package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.DeleteBookmarkService
import io.reactivex.Observable
import javax.inject.Inject

class SendBookmarkDeleteRequest @Inject constructor(
    private val deleteBookmarkService: DeleteBookmarkService
) : UseCase<BaseEntity, SendBookmarkDeleteRequest.Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      deleteBookmarkService
          .requestBookmarkDeletion(params.bookmarkId)

  class Params(val bookmarkId: Int)
}
