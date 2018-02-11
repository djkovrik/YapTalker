package com.sedsoftware.yaptalker.presentation.features.posting.di

import com.sedsoftware.yaptalker.data.repository.YapEmojiRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import com.sedsoftware.yaptalker.presentation.features.posting.AddMessageFragment
import com.sedsoftware.yaptalker.presentation.features.posting.adapter.EmojiClickListener
import dagger.Binds
import dagger.Module

@Module
abstract class AddMessageFragmentModule {

  @FragmentScope
  @Binds
  abstract fun emojiRepository(repository: YapEmojiRepository): EmojiRepository

  @FragmentScope
  @Binds
  abstract fun emojiClickListener(fragment: AddMessageFragment): EmojiClickListener
}
