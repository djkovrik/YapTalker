package com.sedsoftware.yaptalker.domain.interactor.forum

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.UseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetChosenForum @Inject constructor(
    private val chosenForumRepository: ChosenForumRepository
) : UseCaseWithParameter<GetChosenForum.Params, List<BaseEntity>> {

  override fun execute(parameter: Params): Observable<List<BaseEntity>> =
      chosenForumRepository
          .getChosenForum(parameter.forumId, parameter.startPage, parameter.sortingMode)

  class Params(val forumId: Int, val startPage: Int, val sortingMode: String)
}
