package com.sedsoftware.yaptalker.domain.interactor.search

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.SearchTopicsRepository
import io.reactivex.Single
import javax.inject.Inject

class GetSearchResultsPage @Inject constructor(
  private val repository: SearchTopicsRepository
) : SingleUseCaseWithParameter<GetSearchResultsPage.Params, List<BaseEntity>> {

  override fun execute(parameter: Params): Single<List<BaseEntity>> =
    repository
      .getSearchResultsNextPage(
        keyword = parameter.keyword,
        searchId = parameter.searchId,
        searchIn = parameter.searchIn,
        page = parameter.page
      )

  class Params(val keyword: String, val searchId: String, val searchIn: String, val page: Int)
}
