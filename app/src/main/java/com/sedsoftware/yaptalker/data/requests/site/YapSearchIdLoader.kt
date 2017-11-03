package com.sedsoftware.yaptalker.data.requests.site

import io.reactivex.Single
import retrofit2.http.GET

interface YapSearchIdLoader {
  @GET("/act/Search/CODE/getactive")
  fun loadSearchIdHash(): Single<String>
}
