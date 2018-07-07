package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import com.sedsoftware.yaptalker.presentation.model.base.ServerResponseModel
import io.reactivex.functions.Function
import javax.inject.Inject

class ServerResponseModelMapper @Inject constructor() : Function<ServerResponse, ServerResponseModel> {

    override fun apply(response: ServerResponse): ServerResponseModel =
        ServerResponseModel(text = response.text)
}
