package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.ServerResponseModel
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform server response entity from the domain layer into YapEntity in the
 * presentation layer.
 */
class ServerResponseModelMapper @Inject constructor() : Function<BaseEntity, YapEntity> {

  override fun apply(response: BaseEntity): YapEntity {

    response as ServerResponse

    return ServerResponseModel(text = response.text)
  }
}
