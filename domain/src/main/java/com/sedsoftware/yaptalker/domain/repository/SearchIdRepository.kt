package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

/**
 * Interface that represents a Repository for getting search id required to perform active topics request.
 */
interface SearchIdRepository {

  fun getSearchId(): Single<String>
}
