package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

interface SearchIdRepository {
    fun getSearchId(): Single<String>
}
