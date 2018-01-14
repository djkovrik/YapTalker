package com.sedsoftware.yaptalker.data.network.site

import com.sedsoftware.yaptalker.data.parsed.IncubatorPageParsed
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface YapIncubatorLoader {

  /**
   * Load incubator items list from the chosen page.
   *
   * @param startPage Chosen page, starts from zero.
   *
   * @return Parsed incubator page Observable.
   */
  @GET("/st/{startPage}/")
  fun loadIncubator(@Path("startPage") startPage: Int): Observable<IncubatorPageParsed>
}
