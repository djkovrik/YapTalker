package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Observable

interface SitePreferencesRepository {

  fun getSitePreferences(): Observable<BaseEntity>
}
