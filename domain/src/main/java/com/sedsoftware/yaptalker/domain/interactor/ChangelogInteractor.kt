package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.ChangelogRepository
import io.reactivex.Single
import javax.inject.Inject

class ChangelogInteractor @Inject constructor(
  private val repository: ChangelogRepository
) {

  fun getChangelogText(): Single<String> =
    repository
      .getChangelog()
}
