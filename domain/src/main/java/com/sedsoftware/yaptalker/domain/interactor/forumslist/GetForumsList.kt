package com.sedsoftware.yaptalker.domain.interactor.forumslist

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.UseCase
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetForumsList @Inject constructor(
  private val forumsListRepository: ForumsListRepository
) : UseCase<BaseEntity> {

  override fun execute(): Observable<BaseEntity> =
    forumsListRepository
      .getMainForumsList()
}
