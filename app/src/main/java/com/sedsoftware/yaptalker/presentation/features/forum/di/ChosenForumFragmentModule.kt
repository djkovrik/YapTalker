package com.sedsoftware.yaptalker.presentation.features.forum.di

import com.sedsoftware.yaptalker.data.repository.YapChosenForumRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ChosenForumFragmentModule {

  @FragmentScope
  @Binds
  abstract fun chosenForumRepository(repo: YapChosenForumRepository): ChosenForumRepository
}
