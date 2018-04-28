package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class TopicInteractor @Inject constructor(
  private val chosenTopicRepository: ChosenTopicRepository,
  private val bookmarksRepository: BookmarksRepository
) {

  fun getChosenTopic(forumId: Int, topicId: Int, startPage: Int): Single<List<BaseEntity>> =
    chosenTopicRepository
      .getChosenTopic(forumId, topicId, startPage)

  fun requestPostTextForEditing(forumId: Int, topicId: Int, postId: Int, startingPost: Int): Single<EditedPost> =
    chosenTopicRepository
      .requestPostTextForEditing(forumId, topicId, postId, startingPost)

  fun requestPostTextAsQuote(forumId: Int, topicId: Int, postId: Int): Single<QuotedPost> =
    chosenTopicRepository
      .requestPostTextAsQuote(forumId, topicId, postId)

  fun requestBookmarkAdding(topicId: Int, startingPost: Int): Completable =
    bookmarksRepository
      .requestBookmarkAdding(topicId, startingPost)
}
