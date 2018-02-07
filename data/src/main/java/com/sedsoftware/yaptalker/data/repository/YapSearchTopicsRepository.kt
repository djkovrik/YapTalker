package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mappers.SearchPageResultsMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.SearchTopicsRepository
import io.reactivex.Single
import javax.inject.Inject

class YapSearchTopicsRepository @Inject constructor(
  private val dataLoader: YapLoader,
  private val dataMapper: SearchPageResultsMapper
) : SearchTopicsRepository {

  companion object {
    private const val SEARCH_ACT = "Search"
    private const val SEARCH_CODE = "01"
    private const val SEARCH_SUBS = 1
  }

  override fun getSearchResults(
    keyword: String,
    searchIn: String,
    searchHow: String,
    sortBy: String,
    targetForums: List<String>,
    prune: Int
  ): Single<List<BaseEntity>> =
    dataLoader
      .loadSearchedTopics(
        act = SEARCH_ACT,
        code = SEARCH_CODE,
        forums = targetForums,
        keywords = keyword,
        prune = prune,
        searchHow = searchHow,
        searchIn = searchIn,
        searchSubs = SEARCH_SUBS,
        sortBy = sortBy
      )
      .map(dataMapper)

  override fun getSearchResultsNextPage(
    keyword: String,
    searchId: String,
    searchIn: String,
    page: Int
  ): Single<List<BaseEntity>> =
    dataLoader
      .loadSearchedTopicsNextPage(
        act = SEARCH_ACT,
        code = SEARCH_CODE,
        hl = keyword,
        nav = "",
        resultType = "",
        searchIn = searchIn,
        searchId = searchId,
        st = page
      )
      .map(dataMapper)
}
