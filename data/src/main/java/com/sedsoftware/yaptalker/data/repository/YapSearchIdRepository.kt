package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapSearchIdLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import io.reactivex.Single
import javax.inject.Inject

class YapSearchIdRepository @Inject constructor(
    private val searchIdLoader: YapSearchIdLoader,
    private val schedulers: SchedulersProvider
) : SearchIdRepository {

    override fun getSearchId(): Single<String> =
        searchIdLoader
            .loadSearchIdHash()
            .subscribeOn(schedulers.io())
}
