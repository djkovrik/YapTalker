package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.IncubatorPageMapper
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.network.site.YapIncubatorLoader
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapIncubatorRepository @Inject constructor(
  private val dataLoader: YapIncubatorLoader,
  private val dataMapper: IncubatorPageMapper,
  private val listMapper: ListToObservablesMapper
) : IncubatorRepository {

  override fun getIncubatorTopics(page: Int): Observable<BaseEntity> =
    dataLoader
      .loadIncubator(startPage = page)
      .map(dataMapper)
      .flatMap(listMapper)
}
