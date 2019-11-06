package com.sedsoftware.yaptalker.data.network.site

import com.sedsoftware.yaptalker.data.network.site.model.FeedResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YapApi {

    @GET("action/login")
    fun authUser(
        @Query("name") name: String,
        @Query("password") password: String
    ): Single<FeedResult>
}
