package com.sedsoftware.yaptalker.presentation.features.activetopics.di

import com.sedsoftware.yaptalker.data.repository.YapActiveTopicsRepository
import com.sedsoftware.yaptalker.data.repository.YapSearchIdRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.ActiveTopicsRepository
import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.features.activetopics.ActiveTopicsFragment
import com.sedsoftware.yaptalker.presentation.features.activetopics.adapters.ActiveTopicsItemClickListener
import dagger.Binds
import dagger.Module

@Module
abstract class ActiveTopicsFragmentModule {

  @FragmentScope
  @Binds
  abstract fun searchIdRepository(repo: YapSearchIdRepository): SearchIdRepository

  @FragmentScope
  @Binds
  abstract fun thumbnailsRepository(repo: YapActiveTopicsRepository): ActiveTopicsRepository

  @FragmentScope
  @Binds
  abstract fun activeTopicsItemClickListener(fragment: ActiveTopicsFragment): ActiveTopicsItemClickListener

  @FragmentScope
  @Binds
  abstract fun navigationPanelClickListener(fragment: ActiveTopicsFragment): NavigationPanelClickListener
}
