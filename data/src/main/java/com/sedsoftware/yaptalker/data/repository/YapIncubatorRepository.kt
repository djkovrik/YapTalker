package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.IncubatorPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapIncubatorLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.base.IncubatorItem
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import io.reactivex.Single
import javax.inject.Inject

class YapIncubatorRepository @Inject constructor(
    private val dataLoader: YapIncubatorLoader,
    private val dataMapper: IncubatorPageMapper,
    private val schedulers: SchedulersProvider
) : IncubatorRepository {

    override fun getIncubatorTopics(page: Int): Single<List<IncubatorItem>> =
        dataLoader
            .loadIncubator(startPage = page)
            .map(dataMapper)
            .subscribeOn(schedulers.io())
}
