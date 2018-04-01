package com.sedsoftware.yaptalker.presentation.features.gallery.di

import com.sedsoftware.yaptalker.data.repository.YapChosenTopicRepository
import com.sedsoftware.yaptalker.device.sharing.YapSharingHelper
import com.sedsoftware.yaptalker.device.storage.YapImageStorage
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.domain.device.ImageStorage
import com.sedsoftware.yaptalker.domain.device.SharingHelper
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import com.sedsoftware.yaptalker.presentation.features.gallery.TopicGalleryActivity
import com.sedsoftware.yaptalker.presentation.features.gallery.adapter.TopicGalleryLoadMoreClickListener
import com.sedsoftware.yaptalker.presentation.features.topic.GalleryInitialState
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class TopicGalleryActivityModule {

  @Module
  companion object {

    @ActivityScope
    @Provides
    @JvmStatic
    fun provideInitialGalleryState(activity: TopicGalleryActivity): GalleryInitialState =
      activity.galleryInitialState
  }

  @ActivityScope
  @Binds
  abstract fun chosenTopicGalleryRepository(repo: YapChosenTopicRepository): ChosenTopicRepository

  @ActivityScope
  @Binds
  abstract fun galleryClickListener(activity: TopicGalleryActivity): TopicGalleryLoadMoreClickListener

  @ActivityScope
  @Binds
  abstract fun imageSharingHelper(helper: YapSharingHelper): SharingHelper

  @ActivityScope
  @Binds
  abstract fun imageStorage(storage: YapImageStorage): ImageStorage
}
