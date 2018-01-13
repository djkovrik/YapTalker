package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetForumsList @Inject constructor(
    private val forumsListRepository: ForumsListRepository
) : UseCaseOld<BaseEntity, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<BaseEntity> =
      forumsListRepository
          .getMainForumsList()
}
