package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.old.GetNewsList.Params
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetNewsList @Inject constructor(
    private val newsRepository: NewsRepository
) : UseCaseOld<BaseEntity, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      newsRepository
          .getNews(params.pageNumber)

  class Params(val pageNumber: Int)
}
