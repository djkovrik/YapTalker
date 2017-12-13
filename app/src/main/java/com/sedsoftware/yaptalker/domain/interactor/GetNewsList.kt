package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.executor.ExecutionThread
import com.sedsoftware.yaptalker.domain.executor.PostExecutionThread
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class GetNewsList @Inject constructor(
    @Named("io") executionThread: ExecutionThread,
    @Named("ui") postExecutionThread: PostExecutionThread,
    private val newsRepository: NewsRepository
) : UseCase<BaseEntity, GetNewsList.Params>(executionThread, postExecutionThread) {

  override fun buildUseCaseObservable(params: Params): Observable<BaseEntity> =
      newsRepository
          .getNews(params.pageNumber)

  class Params(val pageNumber: Int)
}
