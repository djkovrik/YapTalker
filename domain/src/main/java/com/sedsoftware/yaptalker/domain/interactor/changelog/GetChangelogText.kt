package com.sedsoftware.yaptalker.domain.interactor.changelog

import com.sedsoftware.yaptalker.domain.interactor.SingleUseCase
import com.sedsoftware.yaptalker.domain.repository.ChangelogRepository
import io.reactivex.Single
import javax.inject.Inject

class GetChangelogText @Inject constructor(
  private val repository: ChangelogRepository
) : SingleUseCase<String> {

  override fun execute(): Single<String> =
    repository
      .getChangelog()
}
