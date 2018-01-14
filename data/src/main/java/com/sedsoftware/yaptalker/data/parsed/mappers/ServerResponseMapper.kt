package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * Mapper class used to transform server response body into BaseEntity in the domain layer.
 */
class ServerResponseMapper @Inject constructor() {

  fun transform(response: Response<ResponseBody>): BaseEntity {

    val responseBodyText = response.body()?.string() ?: ""
    return ServerResponse(text = responseBodyText)
  }
}
