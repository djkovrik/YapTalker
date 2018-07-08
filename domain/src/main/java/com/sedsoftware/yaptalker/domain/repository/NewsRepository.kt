package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import io.reactivex.Observable

interface NewsRepository {

    fun getNews(page: Int): Observable<NewsItem>
}
