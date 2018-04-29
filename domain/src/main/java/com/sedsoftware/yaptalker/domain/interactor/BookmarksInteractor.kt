package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class BookmarksInteractor @Inject constructor(
  private val bookmarksRepository: BookmarksRepository
) {

  fun getBookmarks(): Observable<BookmarkedTopic> =
    bookmarksRepository
      .getBookmarks()

  fun deleteFromBookmarks(bookmarkId: Int): Completable =
    bookmarksRepository
      .requestBookmarkDeletion(bookmarkId)
}
