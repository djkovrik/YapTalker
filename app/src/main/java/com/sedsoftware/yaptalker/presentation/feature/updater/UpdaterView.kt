package com.sedsoftware.yaptalker.presentation.feature.updater

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel

@StateStrategyType(SkipStrategy::class)
interface UpdaterView : MvpView, CanShowErrorMessage, CanUpdateUiState {

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "updateInfo")
  fun showUpdateAvailableLabel()

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "updateInfo")
  fun showNoUpdateAvailableLabel()

  fun displayInstalledVersionInfo(versionInfo: AppVersionInfoModel)

  fun displayRemoteVersionInfo(versionInfo: AppVersionInfoModel)

  fun displayLastUpdateCheckDate(date: String)

  fun showCheckingStatus()

  fun showUpdateCompletedStatus()

  fun showUpdateErrorStatus()

  fun showEmptyUpdateStatus()

  fun setUpdateButtonAvailability(isAvailable: Boolean)

  fun setDownloadButtonVisibility(isVisible: Boolean)
}
