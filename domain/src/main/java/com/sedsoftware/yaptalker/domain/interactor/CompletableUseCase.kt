package com.sedsoftware.yaptalker.domain.interactor

import io.reactivex.Completable

interface CompletableUseCase {

  fun execute(): Completable
}
