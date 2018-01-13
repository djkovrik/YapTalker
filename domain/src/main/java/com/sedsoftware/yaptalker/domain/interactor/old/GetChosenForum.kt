package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.GetChosenForum.Params
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetChosenForum @Inject constructor(
    private val chosenForumRepository: ChosenForumRepository
) : UseCaseOld<List<BaseEntity>, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<List<BaseEntity>> =
      chosenForumRepository
          .getChosenForum(params.forumId, params.startPage, params.sortingMode)

  class Params(val forumId: Int, val startPage: Int, val sortingMode: String)
}
