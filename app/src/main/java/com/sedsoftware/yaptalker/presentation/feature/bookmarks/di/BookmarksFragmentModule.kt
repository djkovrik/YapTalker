package com.sedsoftware.yaptalker.presentation.feature.bookmarks.di

import com.sedsoftware.yaptalker.data.repository.YapBookmarksRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.interactor.BookmarksInteractor
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.BookmarksPresenter
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter.BookmarksElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.BookmarksModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class BookmarksFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            interactor: BookmarksInteractor,
            mapper: BookmarksModelMapper,
            schedulers: SchedulersProvider
        ): BookmarksPresenter = BookmarksPresenter(router, interactor, mapper, schedulers)
    }

    @FragmentScope
    @Binds
    abstract fun bookmarksRepository(repository: YapBookmarksRepository): BookmarksRepository

    @FragmentScope
    @Binds
    abstract fun bookmarkElementsClickListener(presenter: BookmarksPresenter): BookmarksElementsClickListener
}
