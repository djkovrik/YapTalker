package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.presentation.mappers.util.DateTransformer
import io.reactivex.functions.Function
import javax.inject.Inject

class LastUpdateDateMapper @Inject constructor(
  private val dateTransformer: DateTransformer
) : Function<Long, String> {

  override fun apply(from: Long): String =
    dateTransformer.transformLongToDateString(from)
}
