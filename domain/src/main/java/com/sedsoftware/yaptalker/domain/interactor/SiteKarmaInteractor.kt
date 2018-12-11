package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Single
import javax.inject.Inject

class SiteKarmaInteractor @Inject constructor(
    private val chosenTopicRepository: ChosenTopicRepository
) {

    fun sendChangeKarmaRequest(
        isTopic: Boolean,
        targetPostId: Int,
        targetTopicId: Int,
        diff: Int
    ): Single<ServerResponse> =
        chosenTopicRepository
            .requestKarmaChange(isTopic, targetPostId, targetTopicId, diff)
}
