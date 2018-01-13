package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.SendBookmarkAddRequest.Params
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Observable
import javax.inject.Inject

class SendBookmarkAddRequest @Inject constructor(
    private val bookmarksRepository: BookmarksRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      bookmarksRepository
          .requestBookmarkAdding(params.topicId, params.startingPost)

  class Params(val topicId: Int, val startingPost: Int)
}
