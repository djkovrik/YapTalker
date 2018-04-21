package com.sedsoftware.yaptalker.presentation.feature.forum.di

import com.sedsoftware.yaptalker.data.repository.YapChosenForumRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.feature.forum.ChosenForumFragment
import com.sedsoftware.yaptalker.presentation.feature.forum.adapter.ChosenForumItemClickListener
import dagger.Binds
import dagger.Module

@Module
abstract class ChosenForumFragmentModule {

  @FragmentScope
  @Binds
  abstract fun chosenForumRepository(repo: YapChosenForumRepository): ChosenForumRepository

  @FragmentScope
  @Binds
  abstract fun chosenForumItemClickListener(fragment: ChosenForumFragment): ChosenForumItemClickListener

  @FragmentScope
  @Binds
  abstract fun navigationPanelClickListener(fragment: ChosenForumFragment): NavigationPanelClickListener
}
