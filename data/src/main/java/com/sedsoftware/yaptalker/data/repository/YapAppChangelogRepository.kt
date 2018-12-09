package com.sedsoftware.yaptalker.data.repository

import android.content.Context
import android.os.Build
import com.sedsoftware.yaptalker.data.network.external.GitHubLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.repository.AppChangelogRepository
import io.reactivex.Single
import javax.inject.Inject

class YapAppChangelogRepository @Inject constructor(
    private val context: Context,
    private val dataLoader: GitHubLoader,
    private val schedulers: SchedulersProvider
) : AppChangelogRepository {

    private companion object {
        const val LOCALE_EN = "en"
    }

    @Suppress("DEPRECATION")
    override fun getChangelog(): Single<String> {

        val resources = context.resources
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {
            resources.configuration.locale
        }

        val callback = if (locale.language == LOCALE_EN) {
            dataLoader.loadChangelogEn()
        } else {
            dataLoader.loadChangelogRu()
        }

        return callback
            .map { response -> response.body()?.string().orEmpty() }
            .subscribeOn(schedulers.io())
    }
}
