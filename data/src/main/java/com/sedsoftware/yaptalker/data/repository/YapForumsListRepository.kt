package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.ForumsListMapper
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.Forum
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapForumsListRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ForumsListMapper,
    private val listMapper: ListToObservablesMapper<Forum>,
    private val settings: Settings,
    private val schedulers: SchedulersProvider
) : ForumsListRepository {

    @Suppress("MagicNumber")
    companion object {
        private val nsfwForumSections = setOf(4, 33, 36)
    }

    override fun getMainForumsList(): Observable<Forum> =
        dataLoader
            .loadForumsList()
            .map(dataMapper)
            .flatMap(listMapper)
            .filter { forumItem ->
                if (settings.isNsfwEnabled())
                    true
                else
                    !nsfwForumSections.contains(forumItem.forumId)
            }
            .subscribeOn(schedulers.io())
}
