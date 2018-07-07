package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ServerResponseMapper @Inject constructor() : Function<Response<ResponseBody>, ServerResponse> {

    override fun apply(from: Response<ResponseBody>): ServerResponse {
        val responseBodyText = from.body()?.string() ?: ""
        return ServerResponse(text = responseBodyText)
    }
}
