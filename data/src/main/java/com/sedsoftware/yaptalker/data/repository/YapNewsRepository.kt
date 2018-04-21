package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.mapper.NewsPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapNewsRepository @Inject constructor(
  private val dataLoader: YapLoader,
  private val dataMapper: NewsPageMapper,
  private val listMapper: ListToObservablesMapper,
  private val settings: Settings
) : NewsRepository {

  private val newsCategories by lazy {
    settings.getNewsCategories()
  }

  override fun getNews(page: Int): Observable<BaseEntity> =
    dataLoader
      .loadNews(page)
      .map(dataMapper)
      .flatMap(listMapper)
      .filter { newsCategories.contains((it as NewsItem).forumLink) }
      .filter { (it as NewsItem).isYapLink }
      .filter { (it as NewsItem).comments != 0 }
}
