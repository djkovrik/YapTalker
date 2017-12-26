package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ActiveTopicsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetActiveTopicsList @Inject constructor(
    private val activeTopicsRepository: ActiveTopicsRepository
) : UseCase<List<BaseEntity>, GetActiveTopicsList.Params> {

  override fun buildUseCaseObservable(params: Params): Observable<List<BaseEntity>> =
      activeTopicsRepository
          .getActiveTopics(params.hash, params.page)

  class Params(val hash: String, val page: Int)
}
