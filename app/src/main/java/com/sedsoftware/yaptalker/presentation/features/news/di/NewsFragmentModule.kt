package com.sedsoftware.yaptalker.presentation.features.news.di

import com.sedsoftware.yaptalker.data.repository.YapNewsRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class NewsFragmentModule {

  @FragmentScope
  @Binds
  abstract fun newsRepository(repo: YapNewsRepository): NewsRepository
}
