package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting search id required to perform active topics request.
 */
interface SearchIdRepository {

  fun getSearchId(): Observable<String>
}
