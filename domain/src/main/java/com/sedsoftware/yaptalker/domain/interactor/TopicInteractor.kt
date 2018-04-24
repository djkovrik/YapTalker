package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
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

  fun requestPostTextForEditing(
    forumId: Int,
    topicId: Int,
    targetPostId: Int,
    startingPost: Int
  ): Single<BaseEntity> =
    chosenTopicRepository
      .requestPostTextForEditing(forumId, topicId, targetPostId, startingPost)

  fun requestPostTextAsQuote(forumId: Int, topicId: Int, targetPostId: Int): Single<BaseEntity> =
    chosenTopicRepository
      .requestPostTextAsQuote(forumId, topicId, targetPostId)

  fun requestBookmarkAdding(topicId: Int, startingPost: Int): Completable =
    bookmarksRepository
      .requestBookmarkAdding(topicId, startingPost)


}