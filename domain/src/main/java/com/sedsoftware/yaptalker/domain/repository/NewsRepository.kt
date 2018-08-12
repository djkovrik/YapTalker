package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import io.reactivex.Single

interface NewsRepository {

    fun getNews(page: Int): Single<List<NewsItem>>
}
