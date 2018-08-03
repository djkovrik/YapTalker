package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.mapper.ForumPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.Topic
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapChosenForumRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ForumPageMapper,
    private val database: YapTalkerDatabase,
    private val schedulers: SchedulersProvider
) : ChosenForumRepository {

    override fun getChosenForum(
        forumId: Int,
        startPageNumber: Int,
        sortingMode: String
    ): Observable<List<BaseEntity>> =
        database
            .getTopicsDao()
            .getBlacklistedTopicIds()
            .flatMapObservable { blacklistedIds ->
                dataLoader
                    .loadForumPage(forumId, startPageNumber, sortingMode)
                    .map(dataMapper)
                    .map { list: List<BaseEntity> ->
                        list.filter { item ->
                            if (item is Topic) {
                                !blacklistedIds.contains(item.id)
                            } else {
                                true
                            }
                        }
                    }
            }
            .subscribeOn(schedulers.io())
}

