package com.sedsoftware.yaptalker.presentation.feature.topic.di

import com.sedsoftware.yaptalker.data.repository.YapBlacklistRepository
import com.sedsoftware.yaptalker.data.repository.YapBookmarksRepository
import com.sedsoftware.yaptalker.data.repository.YapChosenTopicRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.data.repository.YapVideoTokenRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.BlacklistInteractor
import com.sedsoftware.yaptalker.domain.interactor.MessagePostingInteractor
import com.sedsoftware.yaptalker.domain.interactor.SiteKarmaInteractor
import com.sedsoftware.yaptalker.domain.interactor.TopicInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.domain.repository.BlacklistRepository
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.domain.repository.VideoTokenRepository
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.feature.LinkBrowserDelegate
import com.sedsoftware.yaptalker.presentation.feature.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.presentation.feature.topic.ChosenTopicPresenter
import com.sedsoftware.yaptalker.presentation.feature.topic.adapter.ChosenTopicElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.EditedPostModelMapper
import com.sedsoftware.yaptalker.presentation.mapper.QuotedPostModelMapper
import com.sedsoftware.yaptalker.presentation.mapper.ServerResponseModelMapper
import com.sedsoftware.yaptalker.presentation.mapper.TopicModelMapper
import com.sedsoftware.yaptalker.presentation.mapper.TopicStarterMapper
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Suppress("LongParameterList")
@Module
abstract class ChosenTopicFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providesLinkBrowserDelegate(
            router: Router,
            settings: Settings,
            fragment: ChosenTopicFragment
        ): LinkBrowserDelegate = LinkBrowserDelegate(router, settings, fragment.context)

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            settings: Settings,
            topicInteractor: TopicInteractor,
            karmaInteractor: SiteKarmaInteractor,
            postingInteractor: MessagePostingInteractor,
            thumbnailsInteractor: VideoThumbnailsInteractor,
            blacklistInteractor: BlacklistInteractor,
            topicMapper: TopicModelMapper,
            topicStarterMapper: TopicStarterMapper,
            quoteDataMapper: QuotedPostModelMapper,
            editedTextDataMapper: EditedPostModelMapper,
            serverResponseMapper: ServerResponseModelMapper,
            linksDelegate: LinkBrowserDelegate,
            schedulers: SchedulersProvider
        ): ChosenTopicPresenter =

            ChosenTopicPresenter(
                router,
                settings,
                topicInteractor,
                karmaInteractor,
                postingInteractor,
                thumbnailsInteractor,
                blacklistInteractor,
                topicMapper,
                topicStarterMapper,
                quoteDataMapper,
                editedTextDataMapper,
                serverResponseMapper,
                linksDelegate,
                schedulers
            )
    }

    @FragmentScope
    @Binds
    abstract fun chosenTopicRepository(repo: YapChosenTopicRepository): ChosenTopicRepository

    @FragmentScope
    @Binds
    abstract fun thumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

    @FragmentScope
    @Binds
    abstract fun tokenRepository(repo: YapVideoTokenRepository): VideoTokenRepository

    @FragmentScope
    @Binds
    abstract fun topicBookmarksRepository(repository: YapBookmarksRepository): BookmarksRepository

    @FragmentScope
    @Binds
    abstract fun topicBlacklistRepository(repository: YapBlacklistRepository): BlacklistRepository

    @FragmentScope
    @Binds
    abstract fun topicThumbnailsProvider(fragment: ChosenTopicFragment): ThumbnailsProvider

    @FragmentScope
    @Binds
    abstract fun topicElementsClickListener(presenter: ChosenTopicPresenter): ChosenTopicElementsClickListener

    @FragmentScope
    @Binds
    abstract fun topicNavigationClickListener(presenter: ChosenTopicPresenter): NavigationPanelClickListener
}
