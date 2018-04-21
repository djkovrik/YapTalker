package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

/**
 * Interface that represents a Repository for getting changelog.
 */
interface ChangelogRepository {

  fun getChangelog(): Single<String>
}
