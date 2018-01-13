package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendBookmarkAddRequest.Params
import com.sedsoftware.yaptalker.domain.service.AddBookmarkService
import io.reactivex.Observable
import javax.inject.Inject

class SendBookmarkAddRequest @Inject constructor(
    private val addBookmarkService: AddBookmarkService
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      addBookmarkService
          .requestBookmarkAdding(params.topicId, params.startingPost)

  class Params(val topicId: Int, val startingPost: Int)
}
