package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

interface ChangelogRepository {

  fun getChangelog() : Single<String>
}
