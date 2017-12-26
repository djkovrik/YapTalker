package com.sedsoftware.yaptalker.presentation.features.topic.di

import com.sedsoftware.yaptalker.data.repository.YapChosenTopicRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.data.service.YapAddBookmarkService
import com.sedsoftware.yaptalker.data.service.YapKarmaService
import com.sedsoftware.yaptalker.data.service.YapSendMessageService
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.domain.service.AddBookmarkService
import com.sedsoftware.yaptalker.domain.service.KarmaService
import com.sedsoftware.yaptalker.domain.service.SendMessageService
import dagger.Binds
import dagger.Module

@Module
abstract class ChosenTopicFragmentModule {

  @FragmentScope
  @Binds
  abstract fun chosenTopicRepository(repo: YapChosenTopicRepository): ChosenTopicRepository

  @FragmentScope
  @Binds
  abstract fun addBookmarkService(service: YapAddBookmarkService): AddBookmarkService

  @FragmentScope
  @Binds
  abstract fun karmaService(service: YapKarmaService): KarmaService

  @FragmentScope
  @Binds
  abstract fun sendMessageService(service: YapSendMessageService): SendMessageService

  @FragmentScope
  @Binds
  abstract fun thumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository
}
