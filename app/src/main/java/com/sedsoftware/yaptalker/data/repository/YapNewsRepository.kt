package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.data.network.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.NewsPageMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapNewsRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val settings: SettingsManager,
    private val dataMapper: NewsPageMapper
) : NewsRepository {

  private val newsCategories by lazy {
    settings.getNewsCategories()
  }

  override fun getNews(page: Int): Observable<BaseEntity> =
      dataLoader
          .loadNews(startPage = page)
          .map { item -> dataMapper.transform(item) }
          .flatMap { list: List<BaseEntity> -> Observable.fromIterable(list) }
          .filter { entity: BaseEntity ->
            entity as NewsItem
            newsCategories.contains(entity.forumLink)
          }
}
