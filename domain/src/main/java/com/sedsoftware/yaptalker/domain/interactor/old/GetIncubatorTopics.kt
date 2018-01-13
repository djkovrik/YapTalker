package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.GetIncubatorTopics.Params
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetIncubatorTopics @Inject constructor(
    private val incubatorRepository: IncubatorRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      incubatorRepository
          .getIncubatorTopics(params.pageNumber)

  class Params(val pageNumber: Int)
}
