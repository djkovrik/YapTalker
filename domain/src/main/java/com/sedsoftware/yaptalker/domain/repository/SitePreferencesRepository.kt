package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import io.reactivex.Single

/**
 * Interface that represents a Repository for getting user site preferences.
 */
interface SitePreferencesRepository {

  fun getSitePreferences(): Single<BaseEntity>
}
