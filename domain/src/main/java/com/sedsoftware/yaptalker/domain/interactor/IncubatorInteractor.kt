package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.IncubatorItem
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import io.reactivex.Observable
import javax.inject.Inject

class IncubatorInteractor @Inject constructor(
  private val incubatorRepository: IncubatorRepository
) {

  fun getIncubatorPage(pageNumber: Int): Observable<IncubatorItem> =
    incubatorRepository
      .getIncubatorTopics(pageNumber)
}
