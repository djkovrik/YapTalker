package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import com.sedsoftware.yaptalker.domain.repository.AppVersionInfoRepository
import com.sedsoftware.yaptalker.domain.repository.LastUpdateCheckRepository
import io.reactivex.Single
import javax.inject.Inject

class AppUpdaterInteractor @Inject constructor(
    private val versionInfoRepository: AppVersionInfoRepository,
    private val lastUpdateCheckRepository: LastUpdateCheckRepository,
    private val settings: Settings
) {

    fun getInstalledVersionInfo(): Single<VersionInfo> =
        versionInfoRepository
            .getInstalledVersionInfo()

    fun getLastUpdateCheckDate(): Single<Long> =
        lastUpdateCheckRepository
            .getLastUpdateCheckDate()

    fun getRemoteVersionInfo(): Single<VersionInfo> =
        versionInfoRepository
            .getRemoteVersionInfo()
            .doOnSuccess { settings.saveLastUpdateCheckDate() }
}
