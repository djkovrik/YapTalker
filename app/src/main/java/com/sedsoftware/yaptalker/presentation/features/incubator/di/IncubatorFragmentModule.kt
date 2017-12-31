package com.sedsoftware.yaptalker.presentation.features.incubator.di

import com.sedsoftware.yaptalker.data.repository.YapIncubatorRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import dagger.Binds
import dagger.Module

@Module
abstract class IncubatorFragmentModule {

  @FragmentScope
  @Binds
  abstract fun incubatorRepository(repo: YapIncubatorRepository): IncubatorRepository

  @FragmentScope
  @Binds
  abstract fun thumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository
}
