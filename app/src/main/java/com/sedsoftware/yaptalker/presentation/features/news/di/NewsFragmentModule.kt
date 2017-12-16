package com.sedsoftware.yaptalker.presentation.features.news.di

import com.sedsoftware.yaptalker.data.repository.YapNewsRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import dagger.Binds
import dagger.Module

@Module
abstract class NewsFragmentModule {

  @FragmentScope
  @Binds
  abstract fun newsRepository(repo: YapNewsRepository): NewsRepository

  @FragmentScope
  @Binds
  abstract fun thumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository
}
