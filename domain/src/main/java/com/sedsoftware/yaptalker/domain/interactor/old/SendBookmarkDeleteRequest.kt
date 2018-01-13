package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendBookmarkDeleteRequest.Params
import com.sedsoftware.yaptalker.domain.service.DeleteBookmarkService
import io.reactivex.Observable
import javax.inject.Inject

class SendBookmarkDeleteRequest @Inject constructor(
    private val deleteBookmarkService: DeleteBookmarkService
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      deleteBookmarkService
          .requestBookmarkDeletion(params.bookmarkId)

  class Params(val bookmarkId: Int)
}
