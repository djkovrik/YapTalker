package com.sedsoftware.yaptalker.domain.interactor.bookmarks

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.UseCase
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetBookmarks @Inject constructor(
    private val bookmarksRepository: BookmarksRepository
) : UseCase<BaseEntity> {

  override fun execute(): Observable<BaseEntity> =
      bookmarksRepository
          .getBookmarks()
}
