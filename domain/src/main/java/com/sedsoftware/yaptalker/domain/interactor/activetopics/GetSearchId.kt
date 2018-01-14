package com.sedsoftware.yaptalker.domain.interactor.activetopics

import com.sedsoftware.yaptalker.domain.interactor.SingleUseCase
import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import io.reactivex.Single
import javax.inject.Inject

class GetSearchId @Inject constructor(
    private val searchIdRepository: SearchIdRepository
) : SingleUseCase<String> {

  override fun execute(): Single<String> =
      searchIdRepository
          .getSearchId()
}
