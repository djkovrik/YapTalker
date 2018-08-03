package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.repository.LastUpdateCheckRepository
import io.reactivex.Single
import javax.inject.Inject

class YapLastUpdateCheckRepository @Inject constructor(
    private val settings: Settings,
    private val schedulers: SchedulersProvider
) : LastUpdateCheckRepository {

    override fun getLastUpdateCheckDate(): Single<Long> =
        Single
            .just(settings.getLastUpdateCheckDate())
            .subscribeOn(schedulers.io())
}
