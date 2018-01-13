package com.sedsoftware.yaptalker.domain.interactor.topic

import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Completable
import javax.inject.Inject

class SendBookmarkAddRequest @Inject constructor(
    private val bookmarksRepository: BookmarksRepository
) : CompletableUseCaseWithParameter<SendBookmarkAddRequest.Params> {

  override fun execute(parameter: Params): Completable =
      bookmarksRepository
          .requestBookmarkAdding(parameter.topicId, parameter.startingPost)

  class Params(val topicId: Int, val startingPost: Int)
}
