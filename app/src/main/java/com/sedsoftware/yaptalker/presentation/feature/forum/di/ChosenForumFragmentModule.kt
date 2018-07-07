package com.sedsoftware.yaptalker.presentation.feature.forum.di

import com.sedsoftware.yaptalker.data.repository.YapChosenForumRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.ChosenForumInteractor
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.feature.forum.ChosenForumPresenter
import com.sedsoftware.yaptalker.presentation.feature.forum.adapter.ChosenForumItemClickListener
import com.sedsoftware.yaptalker.presentation.mapper.ForumModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class ChosenForumFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            interactor: ChosenForumInteractor,
            mapper: ForumModelMapper,
            settings: Settings
        ): ChosenForumPresenter =
            ChosenForumPresenter(router, interactor, mapper, settings)
    }

    @FragmentScope
    @Binds
    abstract fun chosenForumRepository(repo: YapChosenForumRepository): ChosenForumRepository

    @FragmentScope
    @Binds
    abstract fun chosenForumItemClickListener(presenter: ChosenForumPresenter): ChosenForumItemClickListener

    @FragmentScope
    @Binds
    abstract fun navigationPanelClickListener(presenter: ChosenForumPresenter): NavigationPanelClickListener
}
