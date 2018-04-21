package com.sedsoftware.yaptalker.presentation.feature.posting.di

import com.sedsoftware.yaptalker.data.repository.YapEmojiRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import com.sedsoftware.yaptalker.presentation.feature.posting.AddMessageFragment
import com.sedsoftware.yaptalker.presentation.feature.posting.adapter.EmojiClickListener
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
