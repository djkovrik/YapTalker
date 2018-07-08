package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.SearchTopicsRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchInteractor @Inject constructor(
    private val repository: SearchTopicsRepository
) {

    fun getSearchResults(
        keyword: String,
        searchIn: String,
        searchHow: String,
        sortBy: String,
        targetForums: List<String>,
        prune: Int
    ): Single<List<BaseEntity>> =
        repository
            .getSearchResults(keyword, searchIn, searchHow, sortBy, targetForums, prune)

    fun getTagSearchResults(keyword: String): Single<List<BaseEntity>> =
        repository.getTagSearchResults(keyword)

    fun getSearchResultsNextPage(
        keyword: String,
        searchId: String,
        searchIn: String,
        page: Int
    ): Single<List<BaseEntity>> =
        repository
            .getSearchResultsNextPage(keyword, searchId, searchIn, page)
}
