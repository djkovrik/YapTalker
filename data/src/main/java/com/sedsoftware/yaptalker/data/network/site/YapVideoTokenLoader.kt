package com.sedsoftware.yaptalker.data.network.site

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface YapVideoTokenLoader {
    @GET("/show/{mainId}/{videoId}.mp4.html")
    fun getVideoToken(
        @Path("mainId") mainId: String,
        @Path("videoId") videoId: String
    ): Single<String>
}
