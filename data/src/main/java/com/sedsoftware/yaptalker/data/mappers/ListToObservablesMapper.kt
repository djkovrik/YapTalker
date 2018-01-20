package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable
import io.reactivex.functions.Function
import javax.inject.Inject

class ListToObservablesMapper @Inject constructor() : Function<List<BaseEntity>, Observable<BaseEntity>> {

  override fun apply(from: List<BaseEntity>): Observable<BaseEntity> =
    Observable.fromIterable(from)
}