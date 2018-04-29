package com.sedsoftware.yaptalker.data.mapper

import io.reactivex.Observable
import io.reactivex.functions.Function
import javax.inject.Inject

class ListToObservablesMapper<T> @Inject constructor() : Function<List<T>, Observable<T>> {

  override fun apply(from: List<T>): Observable<T> =
    Observable.fromIterable(from)
}
