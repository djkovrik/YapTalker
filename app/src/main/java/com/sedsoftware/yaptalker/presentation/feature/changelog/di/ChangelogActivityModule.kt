package com.sedsoftware.yaptalker.presentation.feature.changelog.di

import com.sedsoftware.yaptalker.data.repository.YapAppChangelogRepository
import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.domain.repository.AppChangelogRepository
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.changelog.ChangelogActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
abstract class ChangelogActivityModule {

    @Module
    companion object {

        @ActivityScope
        @Provides
        @JvmStatic
        fun provideMessagesDelegate(activity: ChangelogActivity): MessagesDelegate =
            MessagesDelegate(WeakReference(activity))
    }

    @ActivityScope
    @Binds
    abstract fun changelogRepository(repo: YapAppChangelogRepository): AppChangelogRepository
}
