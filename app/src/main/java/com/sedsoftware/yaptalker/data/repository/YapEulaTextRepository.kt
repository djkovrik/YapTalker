package com.sedsoftware.yaptalker.data.repository

import android.content.Context
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.repository.EulaTextRepository
import com.sedsoftware.yaptalker.presentation.commons.extensions.stringRes
import io.reactivex.Observable
import javax.inject.Inject

class YapEulaTextRepository @Inject constructor(
    private val context: Context
) : EulaTextRepository {

  override fun getEulaText(): Observable<String> =
      Observable.just(context.stringRes(R.string.eula_text))
}
