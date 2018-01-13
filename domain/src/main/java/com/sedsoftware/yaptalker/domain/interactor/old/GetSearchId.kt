package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetSearchId @Inject constructor(
    private val searchIdRepository: SearchIdRepository
) : UseCaseOld<String, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<String> =
      searchIdRepository
          .getSearchId()
}
