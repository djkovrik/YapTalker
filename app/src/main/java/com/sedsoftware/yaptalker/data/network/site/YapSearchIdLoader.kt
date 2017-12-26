package com.sedsoftware.yaptalker.data.network.site

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Loads current searchId required to perform active topics request.
 */
interface YapSearchIdLoader {
  @GET("/act/Search/CODE/getactive")
  fun loadSearchIdHash(): Observable<String>
}
