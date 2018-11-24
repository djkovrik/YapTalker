package com.sedsoftware.yaptalker.presentation.feature.news.di

import com.sedsoftware.yaptalker.data.repository.YapBlacklistRepository
import com.sedsoftware.yaptalker.data.repository.YapNewsRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.data.repository.YapVideoTokenRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.BlacklistInteractor
import com.sedsoftware.yaptalker.domain.interactor.NewsInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoTokenInteractor
import com.sedsoftware.yaptalker.domain.repository.BlacklistRepository
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.domain.repository.VideoTokenRepository
import com.sedsoftware.yaptalker.presentation.feature.LinkBrowserDelegate
import com.sedsoftware.yaptalker.presentation.feature.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.feature.news.NewsPresenter
import com.sedsoftware.yaptalker.presentation.feature.news.adapter.NewsItemElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.NewsModelMapper
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class NewsFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providesLinkBrowserDelegate(fragment: NewsFragment): LinkBrowserDelegate =
            LinkBrowserDelegate(fragment.context)

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            settings: Settings,
            getNews: NewsInteractor,
            getThumbnails: VideoThumbnailsInteractor,
            getBlacklist: BlacklistInteractor,
            mapper: NewsModelMapper,
            tokenInteractor: VideoTokenInteractor,
            linksDelegate: LinkBrowserDelegate,
            schedulers: SchedulersProvider
        ): NewsPresenter =
            NewsPresenter(
                router,
                settings,
                getNews,
                getThumbnails,
                getBlacklist,
                mapper,
                tokenInteractor,
                linksDelegate,
                schedulers
            )
    }

    @FragmentScope
    @Binds
    abstract fun newsRepository(repo: YapNewsRepository): NewsRepository

    @FragmentScope
    @Binds
    abstract fun tokenRepository(repo: YapVideoTokenRepository): VideoTokenRepository

    @FragmentScope
    @Binds
    abstract fun newsThumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

    @FragmentScope
    @Binds
    abstract fun topicBlacklistRepository(repository: YapBlacklistRepository): BlacklistRepository

    @FragmentScope
    @Binds
    abstract fun newsThumbnailsProvider(fragment: NewsFragment): ThumbnailsProvider

    @FragmentScope
    @Binds
    abstract fun newsElementsClickListener(presenter: NewsPresenter): NewsItemElementsClickListener
}
