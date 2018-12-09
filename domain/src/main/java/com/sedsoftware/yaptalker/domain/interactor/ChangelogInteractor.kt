package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.AppChangelogRepository
import io.reactivex.Single
import javax.inject.Inject

class ChangelogInteractor @Inject constructor(
    private val repository: AppChangelogRepository
) {

    fun getChangelogText(): Single<String> =
        repository
            .getChangelog()
}
