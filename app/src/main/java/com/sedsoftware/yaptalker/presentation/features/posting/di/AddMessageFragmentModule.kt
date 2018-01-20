package com.sedsoftware.yaptalker.presentation.features.posting.di

import com.sedsoftware.yaptalker.data.repository.YapEmojiRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import dagger.Binds
import dagger.Module

@Module
abstract class AddMessageFragmentModule {

  @FragmentScope
  @Binds
  abstract fun emojiRepository(repository: YapEmojiRepository): EmojiRepository
}
