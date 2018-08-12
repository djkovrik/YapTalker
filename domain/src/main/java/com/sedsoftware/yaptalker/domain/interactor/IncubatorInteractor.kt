package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.IncubatorItem
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import io.reactivex.Single
import javax.inject.Inject

class IncubatorInteractor @Inject constructor(
    private val incubatorRepository: IncubatorRepository
) {

    fun getIncubatorPage(pageNumber: Int): Single<List<IncubatorItem>> =
        incubatorRepository
            .getIncubatorTopics(pageNumber)
}
