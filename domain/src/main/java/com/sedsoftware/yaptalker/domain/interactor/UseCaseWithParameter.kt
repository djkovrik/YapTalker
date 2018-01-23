package com.sedsoftware.yaptalker.domain.interactor

import io.reactivex.Observable

interface UseCaseWithParameter<in P, R> {

  fun execute(parameter: P): Observable<R>
}
