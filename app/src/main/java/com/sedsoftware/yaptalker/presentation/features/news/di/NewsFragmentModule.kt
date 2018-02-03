package com.sedsoftware.yaptalker.presentation.features.news.di

import com.sedsoftware.yaptalker.data.repository.YapNewsRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.presentation.base.thumbnail.ThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.features.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.features.news.adapter.NewsItemElementsClickListener
import dagger.Binds
import dagger.Module

@Module
abstract class NewsFragmentModule {

  @FragmentScope
  @Binds
  abstract fun newsRepository(repo: YapNewsRepository): NewsRepository

  @FragmentScope
  @Binds
  abstract fun newsThumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

  @FragmentScope
  @Binds
  abstract fun newsThumbnailsLoader(fragment: NewsFragment): ThumbnailsLoader

  @FragmentScope
  @Binds
  abstract fun newsElementsClickListener(fragment: NewsFragment): NewsItemElementsClickListener
}
