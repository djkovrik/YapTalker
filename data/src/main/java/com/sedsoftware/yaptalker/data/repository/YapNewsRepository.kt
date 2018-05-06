package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.mapper.NewsPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapNewsRepository @Inject constructor(
  private val dataLoader: YapLoader,
  private val dataMapper: NewsPageMapper,
  private val listMapper: ListToObservablesMapper<NewsItem>,
  private val database: YapTalkerDatabase,
  private val settings: Settings
) : NewsRepository {

  private val newsCategories by lazy {
    settings.getNewsCategories()
  }

  override fun getNews(page: Int): Observable<NewsItem> =
    database
      .getTopicsDao()
      .getBlacklistedTopicIds()
      .flatMapObservable { blacklistedIds ->
        dataLoader
          .loadNews(page)
          .map(dataMapper)
          .flatMap(listMapper)
          .filter { newsCategories.contains(it.forumLink) }
          .filter { it.isYapLink }
          .filter { it.comments != 0 }
          .filter { !blacklistedIds.contains(it.id) }
      }
}
