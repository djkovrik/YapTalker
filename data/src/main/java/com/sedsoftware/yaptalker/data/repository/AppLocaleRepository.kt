package com.sedsoftware.yaptalker.data.repository

import android.content.res.Resources
import android.os.Build
import com.sedsoftware.yaptalker.domain.repository.LocaleRepository
import javax.inject.Inject

@Suppress("DEPRECATION")
class AppLocaleRepository @Inject constructor(
    private val resources: Resources
) : LocaleRepository {

    override fun getCurrentLocale(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0).language
        } else {
            resources.configuration.locale.language
        }
}
