package com.sedsoftware.yaptalker.domain.interactor

import io.reactivex.Observable

/**
 * Interface for a UseCase (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 */

interface UseCase<T, in Params> {

  fun buildUseCaseObservable(params: Params): Observable<T>
}
