package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetBookmarksList @Inject constructor(
    private val bookmarksRepository: BookmarksRepository
) : UseCase<BaseEntity, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<BaseEntity> =
      bookmarksRepository
          .getBookmarks()
}
