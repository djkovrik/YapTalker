package com.sedsoftware.yaptalker.presentation.feature.blacklist.di

import com.sedsoftware.yaptalker.data.repository.YapBlacklistRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.domain.interactor.BlacklistInteractor
import com.sedsoftware.yaptalker.domain.repository.BlacklistRepository
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.blacklist.BlacklistActivity
import com.sedsoftware.yaptalker.presentation.feature.blacklist.BlacklistPresenter
import com.sedsoftware.yaptalker.presentation.feature.blacklist.adapter.BlacklistElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.BlacklistTopicModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
abstract class BlacklistActivityModule {

    @Module
    companion object {

        @ActivityScope
        @Provides
        @JvmStatic
        fun providePresenter(interactor: BlacklistInteractor,
                             mapper: BlacklistTopicModelMapper,
                             schedulers: SchedulersProvider): BlacklistPresenter =
            BlacklistPresenter(interactor, mapper, schedulers)

        @ActivityScope
        @Provides
        @JvmStatic
        fun provideMessagesDelegate(activity: BlacklistActivity): MessagesDelegate =
            MessagesDelegate(WeakReference(activity))
    }

    @ActivityScope
    @Binds
    abstract fun bookmarksRepository(repository: YapBlacklistRepository): BlacklistRepository

    @ActivityScope
    @Binds
    abstract fun bookmarkElementsClickListener(presenter: BlacklistPresenter): BlacklistElementsClickListener
}
