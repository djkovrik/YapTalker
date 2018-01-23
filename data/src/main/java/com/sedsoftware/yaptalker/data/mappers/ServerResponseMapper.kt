package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * Mapper class used to transform server response body into BaseEntity in the domain layer.
 */
class ServerResponseMapper @Inject constructor() : Function<Response<ResponseBody>, BaseEntity> {

  override fun apply(from: Response<ResponseBody>): BaseEntity {
    val responseBodyText = from.body()?.string() ?: ""
    return ServerResponse(text = responseBodyText)
  }
}
