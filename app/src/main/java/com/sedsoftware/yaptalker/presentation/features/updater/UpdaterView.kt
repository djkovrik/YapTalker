package com.sedsoftware.yaptalker.presentation.features.updater

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface UpdaterView : BaseView {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState(forumTitle: String)

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "updateInfo")
  fun showUpdateAvailableLabel()

  @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "updateInfo")
  fun showNoUpdateAvailableLabel()

  fun displayInstalledVersionInfo(version: String)

  fun displayRemoteVersionInfo(version: String)

  fun displayLastUpdateCheckDate(date: String)

  fun showUpdatingStatus()

  fun showUpdateCompletedStatus()

  fun showUpdateErrorStatus()

  fun showEmptyUpdateStatus()
}
