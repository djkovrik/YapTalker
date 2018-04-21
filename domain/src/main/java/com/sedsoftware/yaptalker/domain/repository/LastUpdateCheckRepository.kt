package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

/**
 * Interface that represents a Repository for getting last update checking date.
 */
interface LastUpdateCheckRepository {

  fun getLastUpdateCheckDate(): Single<Long>
}
