package com.sedsoftware.yaptalker.presentation.feature.updater.di

import com.sedsoftware.yaptalker.data.repository.YapAppVersionInfoRepository
import com.sedsoftware.yaptalker.data.repository.YapLastUpdateCheckRepository
import com.sedsoftware.yaptalker.device.storage.YapUpdatesDownloader
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.device.UpdatesDownloader
import com.sedsoftware.yaptalker.domain.repository.AppVersionInfoRepository
import com.sedsoftware.yaptalker.domain.repository.LastUpdateCheckRepository
import dagger.Binds
import dagger.Module

@Module
abstract class UpdaterFragmentModule {

    @FragmentScope
    @Binds
    abstract fun versionInfoRepository(repo: YapAppVersionInfoRepository): AppVersionInfoRepository

    @FragmentScope
    @Binds
    abstract fun lastUpdateInfoRepository(repo: YapLastUpdateCheckRepository): LastUpdateCheckRepository

    @FragmentScope
    @Binds
    abstract fun updatesDownloader(downloader: YapUpdatesDownloader): UpdatesDownloader
}
