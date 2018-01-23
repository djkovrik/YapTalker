package com.sedsoftware.yaptalker.presentation.features.topic.di

import com.sedsoftware.yaptalker.data.repository.YapBookmarksRepository
import com.sedsoftware.yaptalker.data.repository.YapChosenTopicRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ChosenTopicFragmentModule {

  @FragmentScope
  @Binds
  abstract fun chosenTopicRepository(repo: YapChosenTopicRepository): ChosenTopicRepository

  @FragmentScope
  @Binds
  abstract fun thumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

  @FragmentScope
  @Binds
  abstract fun bookmarksRepository(repository: YapBookmarksRepository): BookmarksRepository
}
