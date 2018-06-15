package com.sedsoftware.yaptalker.presentation.feature.mail

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class MailPresenter @Inject constructor(
  private val api: YapLoader
) : BasePresenter<MailView>() {

}
