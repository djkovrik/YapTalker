package com.sedsoftware.yaptalker.domain.interactor.activetopics

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ActiveTopicsRepository
import io.reactivex.Single
import javax.inject.Inject

class GetActiveTopics @Inject constructor(
  private val activeTopicsRepository: ActiveTopicsRepository
) : SingleUseCaseWithParameter<GetActiveTopics.Params, List<BaseEntity>> {

  override fun execute(parameter: Params): Single<List<BaseEntity>> =
    activeTopicsRepository
      .getActiveTopics(parameter.hash, parameter.page)

  class Params(val hash: String, val page: Int)
}
