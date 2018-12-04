package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Single
import javax.inject.Inject

class NewsInteractor @Inject constructor(
    private val newsRepository: NewsRepository
) {

    fun getNewsPage(url: String): Single<List<NewsItem>> =
        newsRepository
            .getNews(url)
}
