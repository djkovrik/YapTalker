package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Single

interface SearchTopicsRepository {

  fun getSearchResults(
    keyword: String,
    searchIn: String,
    searchHow: String,
    sortBy: String,
    targetForums: List<String>,
    prune: Int
  ): Single<List<BaseEntity>>

  fun getSearchResultsNextPage(
    keyword: String,
    searchId: String,
    searchIn: String,
    page: Int
  ): Single<List<BaseEntity>>
}
