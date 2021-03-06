package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.mapper.NewsPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import io.reactivex.Single
import javax.inject.Inject

class YapNewsRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: NewsPageMapper,
    private val listMapper: ListToObservablesMapper<NewsItem>,
    private val database: YapTalkerDatabase,
    private val settings: Settings,
    private val schedulers: SchedulersProvider
) : NewsRepository {

    private val newsCategories by lazy {
        settings.getNewsCategories()
    }

    private val mainPageUrl: String = "www.yaplakal.com"
    private var currentUrl: String = ""
    private var currentOffset: String = ""

    override fun getNews(url: String): Single<List<NewsItem>> =
        database
            .getTopicsDao()
            .getBlacklistedTopicIds()
            .flatMapObservable { blacklistedIds ->
                dataLoader
                    .loadNews(url)
                    .map(dataMapper)
                    .flatMap { newsBlock ->
                        currentUrl = url
                        currentOffset = newsBlock.offset
                        listMapper.apply(newsBlock.items)
                    }
                    .filter {
                        if (url.contains(mainPageUrl)) {
                            newsCategories.contains(it.forumLink)
                        } else {
                            true
                        }
                    }
                    .filter { it.isYapLink }
                    .filter { it.comments != 0 }
                    .filter { !blacklistedIds.contains(it.id) }
            }
            .toList()
            .subscribeOn(schedulers.io())

    override fun getNewsNextPage(): Single<List<NewsItem>> =
        database
            .getTopicsDao()
            .getBlacklistedTopicIds()
            .flatMapObservable { blacklistedIds ->
                val newUrl = "$currentUrl/st/$currentOffset"
                dataLoader
                    .loadNews(newUrl)
                    .map(dataMapper)
                    .flatMap { newsBlock ->
                        currentOffset = newsBlock.offset
                        listMapper.apply(newsBlock.items)
                    }
                    .filter {
                        if (currentUrl.contains(mainPageUrl)) {
                            newsCategories.contains(it.forumLink)
                        } else {
                            true
                        }
                    }
                    .filter { it.isYapLink }
                    .filter { it.comments != 0 }
                    .filter { !blacklistedIds.contains(it.id) }
            }
            .toList()
            .subscribeOn(schedulers.io())
}
