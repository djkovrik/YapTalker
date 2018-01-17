package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapIncubatorLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.IncubatorPageMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapIncubatorRepository @Inject constructor(
  private val dataLoader: YapIncubatorLoader,
  private val dataMapper: IncubatorPageMapper
) : IncubatorRepository {

  override fun getIncubatorTopics(page: Int): Observable<BaseEntity> =
    dataLoader
      .loadIncubator(startPage = page)
      .map { parsedIncubatorPage -> dataMapper.transform(parsedIncubatorPage) }
      .flatMap { topicsList -> Observable.fromIterable(topicsList) }
}
