package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

interface AppChangelogRepository {
    fun getChangelogEn(): Single<String>
    fun getChangelogRu(): Single<String>
}
