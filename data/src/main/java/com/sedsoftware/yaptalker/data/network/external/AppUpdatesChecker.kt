package com.sedsoftware.yaptalker.data.network.external

import com.sedsoftware.yaptalker.data.parsed.AppVersionInfo
import io.reactivex.Single
import retrofit2.http.GET

interface AppUpdatesChecker {

    @GET("/yaptalker/info.json")
    fun loadCurrentVersionInfo(): Single<AppVersionInfo>
}
