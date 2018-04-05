package com.sedsoftware.yaptalker.presentation.features.changelog.di

import com.sedsoftware.yaptalker.data.repository.AppChangelogRepository
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.domain.repository.ChangelogRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ChangelogActivityModule {

  @ActivityScope
  @Binds
  abstract fun changelogRepository(repo: AppChangelogRepository): ChangelogRepository
}