package com.sedsoftware.yaptalker.presentation.features.gallery.di

import com.sedsoftware.yaptalker.data.repository.YapChosenTopicRepository
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import com.sedsoftware.yaptalker.presentation.features.gallery.TopicGalleryActivity
import com.sedsoftware.yaptalker.presentation.features.gallery.adapter.TopicGalleryLoadMoreClickListener
import dagger.Binds
import dagger.Module

@Module
abstract class TopicGalleryActivityModule {

  @ActivityScope
  @Binds
  abstract fun chosenTopicGalleryRepository(repo: YapChosenTopicRepository): ChosenTopicRepository

  @ActivityScope
  @Binds
  abstract fun galleryClickListener(activity: TopicGalleryActivity): TopicGalleryLoadMoreClickListener
}
