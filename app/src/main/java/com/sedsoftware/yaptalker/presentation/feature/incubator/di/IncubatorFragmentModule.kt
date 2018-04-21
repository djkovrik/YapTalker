package com.sedsoftware.yaptalker.presentation.feature.incubator.di

import com.sedsoftware.yaptalker.data.repository.YapIncubatorRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.presentation.base.thumbnail.ThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.feature.incubator.IncubatorFragment
import com.sedsoftware.yaptalker.presentation.feature.incubator.adapter.IncubatorElementsClickListener
import dagger.Binds
import dagger.Module

@Module
abstract class IncubatorFragmentModule {

  @FragmentScope
  @Binds
  abstract fun incubatorRepository(repo: YapIncubatorRepository): IncubatorRepository

  @FragmentScope
  @Binds
  abstract fun incubatorThumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

  @FragmentScope
  @Binds
  abstract fun incubatorThumbnailsLoader(fragment: IncubatorFragment): ThumbnailsLoader

  @FragmentScope
  @Binds
  abstract fun incubatorElementsClickListener(fragment: IncubatorFragment): IncubatorElementsClickListener
}
