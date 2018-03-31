package com.sedsoftware.yaptalker.presentation.features.updater

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel

@StateStrategyType(SkipStrategy::class)
interface UpdaterView : BaseView {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState()

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "updateInfo")
  fun showUpdateAvailableLabel()

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "updateInfo")
  fun showNoUpdateAvailableLabel()

  fun displayInstalledVersionInfo(versionInfo: AppVersionInfoModel)

  fun displayRemoteVersionInfo(versionInfo: AppVersionInfoModel)

  fun displayLastUpdateCheckDate(versionInfo: AppVersionInfoModel)

  fun showCheckingStatus()

  fun showUpdateCompletedStatus()

  fun showUpdateErrorStatus()

  fun showEmptyUpdateStatus()

  fun setUpdateButtonAvailability(isAvailable: Boolean)

  fun setDownloadButtonVisibility(isVisible: Boolean)
}
