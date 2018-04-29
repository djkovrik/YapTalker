package com.sedsoftware.yaptalker.presentation.feature.activetopics.di

import com.sedsoftware.yaptalker.data.repository.YapActiveTopicsRepository
import com.sedsoftware.yaptalker.data.repository.YapSearchIdRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.interactor.ActiveTopicsInteractor
import com.sedsoftware.yaptalker.domain.repository.ActiveTopicsRepository
import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.feature.activetopics.ActiveTopicsPresenter
import com.sedsoftware.yaptalker.presentation.feature.activetopics.adapter.ActiveTopicsItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.ActiveTopicModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class ActiveTopicsFragmentModule {

  @Module
  companion object {

    @FragmentScope
    @Provides
    @JvmStatic
    fun providePresenter(router: Router,
                         interactor: ActiveTopicsInteractor,
                         mapper: ActiveTopicModelMapper): ActiveTopicsPresenter =
      ActiveTopicsPresenter(router, interactor, mapper)
  }

  @FragmentScope
  @Binds
  abstract fun searchIdRepository(repo: YapSearchIdRepository): SearchIdRepository

  @FragmentScope
  @Binds
  abstract fun thumbnailsRepository(repo: YapActiveTopicsRepository): ActiveTopicsRepository

  @FragmentScope
  @Binds
  abstract fun activeTopicsItemClickListener(presenter: ActiveTopicsPresenter): ActiveTopicsItemClickListener

  @FragmentScope
  @Binds
  abstract fun navigationPanelClickListener(presenter: ActiveTopicsPresenter): NavigationPanelClickListener
}
