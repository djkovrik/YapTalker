package com.sedsoftware.yaptalker.presentation.feature.bookmarks.di

import com.sedsoftware.yaptalker.data.repository.YapBookmarksRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.interactor.BookmarksInteractor
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.BookmarksPresenter
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapters.BookmarkElementsClickListener
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
    fun providePresenter(router: Router,
                         interactor: BookmarksInteractor,
                         mapper: BookmarksModelMapper): BookmarksPresenter =
      BookmarksPresenter(router, interactor, mapper)
  }

  @FragmentScope
  @Binds
  abstract fun bookmarksRepository(repository: YapBookmarksRepository): BookmarksRepository

  @FragmentScope
  @Binds
  abstract fun bookmarkElementsClickListener(presenter: BookmarksPresenter): BookmarkElementsClickListener
}
