package com.sedsoftware.yaptalker.domain.interactor.incubator

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.UseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetIncubatorTopics @Inject constructor(
    private val incubatorRepository: IncubatorRepository
) : UseCaseWithParameter<GetIncubatorTopics.Params, BaseEntity> {

  override fun execute(parameter: Params): Observable<BaseEntity> =
      incubatorRepository
          .getIncubatorTopics(parameter.pageNumber)

  class Params(val pageNumber: Int)
}
