package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import io.reactivex.Observable
import javax.inject.Inject

class ForumsListInteractor @Inject constructor(
  private val forumsListRepository: ForumsListRepository
) {

  fun getForumsList(): Observable<BaseEntity> =
    forumsListRepository
      .getMainForumsList()
}
