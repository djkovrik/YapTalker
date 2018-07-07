package com.sedsoftware.yaptalker.data.network.external

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface GitHubLoader {

    @GET("/djkovrik/YapTalker/master/docs/CHANGELOG.md")
    fun loadChangelogEn(): Single<Response<ResponseBody>>

    @GET("/djkovrik/YapTalker/master/docs/CHANGELOG_RU.md")
    fun loadChangelogRu(): Single<Response<ResponseBody>>
}
