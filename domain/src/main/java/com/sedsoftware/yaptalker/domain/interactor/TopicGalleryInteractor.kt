package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Single
import javax.inject.Inject

class TopicGalleryInteractor @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) {

    fun getTopicGallery(forumId: Int, topicId: Int, startPage: Int): Single<List<BaseEntity>> =
        chosenTopicRepository
            .getChosenTopic(forumId, topicId, startPage)
}
