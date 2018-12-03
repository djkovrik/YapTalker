package com.sedsoftware.yaptalker.presentation.feature.incubator.di

import com.sedsoftware.yaptalker.data.repository.YapIncubatorRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.data.repository.YapVideoTokenRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.IncubatorInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoTokenInteractor
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.domain.repository.VideoTokenRepository
import com.sedsoftware.yaptalker.presentation.feature.LinkBrowserDelegate
import com.sedsoftware.yaptalker.presentation.feature.incubator.IncubatorFragment
import com.sedsoftware.yaptalker.presentation.feature.incubator.IncubatorPresenter
import com.sedsoftware.yaptalker.presentation.feature.incubator.adapter.IncubatorElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.IncubatorModelMapper
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class IncubatorFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providesLinkBrowserDelegate(
            router: Router,
            tokenInteractor: VideoTokenInteractor,
            settings: Settings,
            fragment: IncubatorFragment
        ): LinkBrowserDelegate = LinkBrowserDelegate(router, tokenInteractor, settings, fragment.context)

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            incubatorInteractor: IncubatorInteractor,
            videoThumbnailsInteractor: VideoThumbnailsInteractor,
            mapper: IncubatorModelMapper,
            linksDelegate: LinkBrowserDelegate,
            schedulers: SchedulersProvider
        ): IncubatorPresenter =
            IncubatorPresenter(
                router,
                incubatorInteractor,
                videoThumbnailsInteractor,
                mapper,
                linksDelegate,
                schedulers
            )
    }

    @FragmentScope
    @Binds
    abstract fun incubatorRepository(repo: YapIncubatorRepository): IncubatorRepository

    @FragmentScope
    @Binds
    abstract fun tokenRepository(repo: YapVideoTokenRepository): VideoTokenRepository

    @FragmentScope
    @Binds
    abstract fun incubatorThumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

    @FragmentScope
    @Binds
    abstract fun incubatorThumbnailsProvider(fragment: IncubatorFragment): ThumbnailsProvider

    @FragmentScope
    @Binds
    abstract fun incubatorElementsClickListener(presenter: IncubatorPresenter): IncubatorElementsClickListener
}
