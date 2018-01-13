package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetSearchId @Inject constructor(
    private val searchIdRepository: SearchIdRepository
) : UseCase<String, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<String> =
      searchIdRepository
          .getSearchId()
}
