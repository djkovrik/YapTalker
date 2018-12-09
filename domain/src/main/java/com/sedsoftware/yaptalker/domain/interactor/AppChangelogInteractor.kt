package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.AppChangelogRepository
import com.sedsoftware.yaptalker.domain.repository.LocaleRepository
import io.reactivex.Single
import javax.inject.Inject

class AppChangelogInteractor @Inject constructor(
    private val changelogRepository: AppChangelogRepository,
    private val localeRepository: LocaleRepository
) {

    fun getChangelogText(): Single<String> =
        if (localeRepository.getCurrentLocale() == LOCALE_EN) {
            changelogRepository.getChangelogEn()
        } else {
            changelogRepository.getChangelogRu()
        }

    private companion object {
        const val LOCALE_EN = "en"
    }
}
