package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.mapper.ActiveTopicsPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ActiveTopicsRepository
import io.reactivex.Single
import javax.inject.Inject

class YapActiveTopicsRepository @Inject constructor(
  private val dataLoader: YapLoader,
  private val dataMapper: ActiveTopicsPageMapper,
  private val database: YapTalkerDatabase
) : ActiveTopicsRepository {

  companion object {
    private const val ACTIVE_TOPICS_ACT = "Search"
    private const val ACTIVE_TOPICS_CODE = "getactive"
  }

  override fun getActiveTopics(hash: String, page: Int): Single<List<BaseEntity>> =
    dataLoader
      .loadActiveTopics(
        act = ACTIVE_TOPICS_ACT,
        code = ACTIVE_TOPICS_CODE,
        searchId = hash,
        startTopicNumber = page
      )
      .map(dataMapper)
}
