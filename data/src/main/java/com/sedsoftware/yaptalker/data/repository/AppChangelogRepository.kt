package com.sedsoftware.yaptalker.data.repository

import android.content.Context
import android.os.Build
import com.sedsoftware.yaptalker.data.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.data.network.external.GitHubLoader
import com.sedsoftware.yaptalker.domain.repository.ChangelogRepository
import io.reactivex.Single
import javax.inject.Inject

class AppChangelogRepository @Inject constructor(
  private val context: Context,
  private val dataLoader: GitHubLoader,
  private val dataMapper: ServerResponseMapper
) : ChangelogRepository {

  private companion object {
    const val LOCALE_RU = "ru_RU"
  }

  override fun getChangelog(): Single<String> {

    val resources = context.resources
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      resources.configuration.locales.get(0)
    } else {
      //noinspection deprecation
      resources.configuration.locale
    }

    return if (locale.country == LOCALE_RU) {
      dataLoader.loadChangelogRu()
    } else {
      dataLoader.loadChangelogEn()
    }
      .map { response -> response.body()?.string() ?: "" }
  }
}
