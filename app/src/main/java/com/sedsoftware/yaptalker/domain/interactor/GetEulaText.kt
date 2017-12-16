package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.EulaTextRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetEulaText @Inject constructor(
    private val eulaTextRepository: EulaTextRepository
) : UseCase<String, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<String> =
      eulaTextRepository
          .getEulaText()
}