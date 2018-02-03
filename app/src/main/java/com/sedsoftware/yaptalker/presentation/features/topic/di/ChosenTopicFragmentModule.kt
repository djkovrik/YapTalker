package com.sedsoftware.yaptalker.presentation.features.topic.di

import com.sedsoftware.yaptalker.data.repository.YapBookmarksRepository
import com.sedsoftware.yaptalker.data.repository.YapChosenTopicRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.base.thumbnail.ThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.features.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.presentation.features.topic.adapter.ChosenTopicElementsClickListener
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
  abstract fun topicBookmarksRepository(repository: YapBookmarksRepository): BookmarksRepository

  @FragmentScope
  @Binds
  abstract fun topicElementsClickListener(fragment: ChosenTopicFragment): ChosenTopicElementsClickListener

  @FragmentScope
  @Binds
  abstract fun topicNavigationClickListener(fragment: ChosenTopicFragment): NavigationPanelClickListener

  @FragmentScope
  @Binds
  abstract fun topicThumbnailsLoader(fragment: ChosenTopicFragment): ThumbnailsLoader
}
