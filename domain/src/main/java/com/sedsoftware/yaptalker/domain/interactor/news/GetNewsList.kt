package com.sedsoftware.yaptalker.domain.interactor.news

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.UseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetNewsList @Inject constructor(
  private val newsRepository: NewsRepository
) : UseCaseWithParameter<GetNewsList.Params, BaseEntity> {

  override fun execute(parameter: Params): Observable<BaseEntity> =
    newsRepository
      .getNews(parameter.pageNumber)

  class Params(val pageNumber: Int)
}
