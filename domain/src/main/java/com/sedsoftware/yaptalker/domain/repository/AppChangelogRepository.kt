package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

interface AppChangelogRepository {
    fun getChangelog(): Single<String>
}
