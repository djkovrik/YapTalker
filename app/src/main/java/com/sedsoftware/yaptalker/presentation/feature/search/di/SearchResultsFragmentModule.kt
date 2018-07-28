package com.sedsoftware.yaptalker.presentation.feature.search.di

import com.sedsoftware.yaptalker.data.repository.YapSearchTopicsRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.interactor.SearchInteractor
import com.sedsoftware.yaptalker.domain.repository.SearchTopicsRepository
import com.sedsoftware.yaptalker.presentation.feature.search.SearchResultsPresenter
import com.sedsoftware.yaptalker.presentation.feature.search.adapter.SearchResultsItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.SearchResultsModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class SearchResultsFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            interactor: SearchInteractor,
            mapper: SearchResultsModelMapper,
            schedulers: SchedulersProvider
        ): SearchResultsPresenter =
            SearchResultsPresenter(router, interactor, mapper, schedulers)
    }

    @FragmentScope
    @Binds
    abstract fun searchRepository(repo: YapSearchTopicsRepository): SearchTopicsRepository

    @FragmentScope
    @Binds
    abstract fun searchResultsClickListener(presenter: SearchResultsPresenter): SearchResultsItemClickListener
}
