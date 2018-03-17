package com.sedsoftware.yaptalker.presentation.features.changelog.di

import com.sedsoftware.yaptalker.data.repository.AppChangelogRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.ChangelogRepository
import dagger.Binds
import dagger.Module

@Module
interface ChangelogFragmentModule {

  @FragmentScope
  @Binds
  fun changelogRepository(repo: AppChangelogRepository): ChangelogRepository
}
