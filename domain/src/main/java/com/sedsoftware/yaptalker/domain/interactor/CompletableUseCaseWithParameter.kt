package com.sedsoftware.yaptalker.domain.interactor

import io.reactivex.Completable

interface CompletableUseCaseWithParameter<in P> {

  fun execute(parameter: P): Completable
}
