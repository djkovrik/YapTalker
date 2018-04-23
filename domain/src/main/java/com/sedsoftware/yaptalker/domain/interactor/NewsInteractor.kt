package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class NewsInteractor @Inject constructor(
  private val newsRepository: NewsRepository
) {

  fun getNewsPage(pageNumber: Int): Observable<BaseEntity> =
    newsRepository
      .getNews(pageNumber)
}
