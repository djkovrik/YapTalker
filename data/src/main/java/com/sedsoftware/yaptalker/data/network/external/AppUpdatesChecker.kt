package com.sedsoftware.yaptalker.data.network.external

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface AppUpdatesChecker {

  @GET("/yaptalker/info.json")
  fun loadCurrentVersionInfo(): Single<Response<ResponseBody>>
}
