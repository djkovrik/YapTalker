package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting raw EULA text.
 */
interface EulaTextRepository {

  fun getEulaText(): Observable<String>
}
