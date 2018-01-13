package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.SitePreferencesRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetSiteUserPreferences @Inject constructor(
    private val siteSettingsRepository: SitePreferencesRepository
) : UseCaseOld<BaseEntity, Unit> {

  override fun buildUseCaseObservable(params: Unit): Observable<BaseEntity> =
      siteSettingsRepository
          .getSitePreferences()
}
