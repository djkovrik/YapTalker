package com.sedsoftware.yaptalker.presentation.feature.forumslist.di

import com.sedsoftware.yaptalker.data.repository.YapForumsListRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.interactor.ForumsListInteractor
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import com.sedsoftware.yaptalker.presentation.feature.forumslist.ForumsPresenter
import com.sedsoftware.yaptalker.presentation.feature.forumslist.adapter.ForumsItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.ForumsListModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class ForumsFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            interactor: ForumsListInteractor,
            mapper: ForumsListModelMapper
        ): ForumsPresenter =
            ForumsPresenter(router, interactor, mapper)
    }

    @FragmentScope
    @Binds
    abstract fun forumsListRepository(repository: YapForumsListRepository): ForumsListRepository

    @FragmentScope
    @Binds
    abstract fun forumsItemClickListener(presenter: ForumsPresenter): ForumsItemClickListener
}
