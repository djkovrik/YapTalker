package com.sedsoftware.yaptalker.presentation.features.updater.di

import com.sedsoftware.yaptalker.data.repository.AppVersionInfoRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.VersionInfoRepository
import dagger.Binds
import dagger.Module

@Module
abstract class UpdaterFragmentModule {

  @FragmentScope
  @Binds
  abstract fun versionInfoRepository(repo: AppVersionInfoRepository): VersionInfoRepository
}
