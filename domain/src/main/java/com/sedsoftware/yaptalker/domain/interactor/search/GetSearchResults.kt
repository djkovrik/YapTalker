package com.sedsoftware.yaptalker.domain.interactor.search

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.SearchTopicsRepository
import io.reactivex.Single
import javax.inject.Inject

class GetSearchResults @Inject constructor(
  private val repository: SearchTopicsRepository
) : SingleUseCaseWithParameter<GetSearchResults.Params, List<BaseEntity>> {

  override fun execute(parameter: Params): Single<List<BaseEntity>> =
    repository
      .getSearchResults(
        keyword = parameter.keyword,
        searchIn = parameter.searchIn,
        searchHow = parameter.searchHow,
        sortBy = parameter.sortBy,
        targetForums = parameter.targetForums,
        prune = parameter.prune
      )

  class Params(
    val keyword: String,
    val searchIn: String,
    val searchHow: String,
    val sortBy: String,
    val targetForums: List<String>,
    val prune: Int
  )
}
