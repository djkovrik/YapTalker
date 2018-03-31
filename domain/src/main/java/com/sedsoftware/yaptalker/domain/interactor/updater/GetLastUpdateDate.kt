package com.sedsoftware.yaptalker.domain.interactor.updater

import com.sedsoftware.yaptalker.domain.interactor.SingleUseCase
import com.sedsoftware.yaptalker.domain.repository.LastUpdateCheckRepository
import io.reactivex.Single
import javax.inject.Inject

class GetLastUpdateDate @Inject constructor(
  private val repository: LastUpdateCheckRepository
) : SingleUseCase<Long> {

  override fun execute(): Single<Long> =
    repository
      .getLastUpdateCheckDate()
}
