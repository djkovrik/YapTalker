package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting news related data.
 */
interface NewsRepository {

  fun getNews(page: Int): Observable<List<BaseEntity>>
}
