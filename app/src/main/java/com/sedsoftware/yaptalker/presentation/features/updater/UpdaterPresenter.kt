package com.sedsoftware.yaptalker.presentation.features.updater

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.updater.GetInstalledVersionInfo
import com.sedsoftware.yaptalker.domain.interactor.updater.GetRemoteVersionInfo
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.mappers.VersionInfoMapper
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class UpdaterPresenter @Inject constructor(
  private val router: Router,
  private val installedVersionUseCase: GetInstalledVersionInfo,
  private val remoteVersionUseCase: GetRemoteVersionInfo,
  private val versionInfoMapper: VersionInfoMapper
) : BasePresenter<UpdaterView>() {


}
