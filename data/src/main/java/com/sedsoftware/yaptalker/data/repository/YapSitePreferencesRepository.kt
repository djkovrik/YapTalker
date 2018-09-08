package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.mapper.SettingsPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.base.SitePreferences
import com.sedsoftware.yaptalker.domain.repository.SitePreferencesRepository
import io.reactivex.Single
import javax.inject.Inject

class YapSitePreferencesRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: SettingsPageMapper,
    private val schedulers: SchedulersProvider
) : SitePreferencesRepository {

    companion object {
        private const val SETTINGS_ACT = "UserCP"
        private const val SETTINGS_CODE = "04"
    }

    override fun getSitePreferences(): Single<SitePreferences> =
        dataLoader
            .loadSitePreferences(
                act = SETTINGS_ACT,
                code = SETTINGS_CODE
            )
            .map(dataMapper)
            .subscribeOn(schedulers.io())
}
