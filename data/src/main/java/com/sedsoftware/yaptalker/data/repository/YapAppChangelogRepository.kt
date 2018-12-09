package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.external.GitHubLoader
import com.sedsoftware.yaptalker.domain.repository.AppChangelogRepository
import io.reactivex.Single
import javax.inject.Inject

class YapAppChangelogRepository @Inject constructor(
    private val dataLoader: GitHubLoader
) : AppChangelogRepository {

    override fun getChangelogEn(): Single<String> =
        dataLoader
            .loadChangelogEn()
            .map { response -> response.body()?.string().orEmpty() }

    override fun getChangelogRu(): Single<String> =
        dataLoader
            .loadChangelogRu()
            .map { response -> response.body()?.string().orEmpty() }
}
