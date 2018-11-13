package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapVideoTokenLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.repository.VideoTokenRepository
import io.reactivex.Single
import javax.inject.Inject

class YapVideoTokenRepository @Inject constructor(
    private val dataLoader: YapVideoTokenLoader,
    private val schedulers: SchedulersProvider
) : VideoTokenRepository {

    override fun getVideoToken(mainId: String, videoId: String): Single<String> =
        dataLoader
            .getVideoToken(mainId, videoId)
            .subscribeOn(schedulers.io())
}
