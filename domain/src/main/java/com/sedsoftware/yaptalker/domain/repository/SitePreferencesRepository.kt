package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import io.reactivex.Single

interface SitePreferencesRepository {

  fun getSitePreferences(): Single<SitePreferences>
}
