package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.NewsPageMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapNewsRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: NewsPageMapper
) : NewsRepository {

  override fun getNews(page: Int): Observable<List<BaseEntity>> =
      dataLoader
          .loadNews(startPage = page)
          .map { item -> dataMapper.transform(item) }
}
