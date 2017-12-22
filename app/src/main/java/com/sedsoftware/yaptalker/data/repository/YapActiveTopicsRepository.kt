package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ActiveTopicsPageMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ActiveTopicsRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapActiveTopicsRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ActiveTopicsPageMapper
) : ActiveTopicsRepository {

  companion object {
    private const val ACTIVE_TOPICS_ACT = "Search"
    private const val ACTIVE_TOPICS_CODE = "getactive"
  }

  override fun getActiveTopics(hash: String, page: Int): Observable<BaseEntity> =
      dataLoader
          .loadActiveTopics(
              act = ACTIVE_TOPICS_ACT,
              code = ACTIVE_TOPICS_CODE,
              searchId = hash,
              startTopicNumber = page
          )
          .map { item -> dataMapper.transform(item) }
          .flatMap { list -> Observable.fromIterable(list) }
}