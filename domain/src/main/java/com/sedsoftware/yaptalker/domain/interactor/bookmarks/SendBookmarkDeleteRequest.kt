package com.sedsoftware.yaptalker.domain.interactor.bookmarks

import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Completable
import javax.inject.Inject

class SendBookmarkDeleteRequest @Inject constructor(
    private val bookmarksRepository: BookmarksRepository
) : CompletableUseCaseWithParameter<SendBookmarkDeleteRequest.Params> {

  override fun execute(parameter: Params): Completable =
      bookmarksRepository
          .requestBookmarkDeletion(parameter.bookmarkId)

  class Params(val bookmarkId: Int)
}
