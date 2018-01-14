package com.sedsoftware.yaptalker.domain.interactor

import io.reactivex.Observable

interface UseCase<T> {

  fun execute(): Observable<T>
}
