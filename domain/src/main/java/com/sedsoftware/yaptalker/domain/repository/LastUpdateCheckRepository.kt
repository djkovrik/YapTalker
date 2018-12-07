package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

interface LastUpdateCheckRepository {
    fun getLastUpdateCheckDate(): Single<Long>
}
