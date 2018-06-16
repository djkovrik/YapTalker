package com.sedsoftware.yaptalker.presentation.feature.mail

import com.arellomobile.mvp.MvpView
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanShowLoadingIndicator
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState

interface MailView : MvpView, CanShowErrorMessage, CanShowLoadingIndicator, CanUpdateUiState {

}
