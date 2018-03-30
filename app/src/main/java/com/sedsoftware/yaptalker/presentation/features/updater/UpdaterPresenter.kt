package com.sedsoftware.yaptalker.presentation.features.updater

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.updater.GetInstalledVersionInfo
import com.sedsoftware.yaptalker.domain.interactor.updater.GetRemoteVersionInfo
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.ConnectionState
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mappers.VersionInfoMapper
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class UpdaterPresenter @Inject constructor(
  private val router: Router,
  private val installedVersionUseCase: GetInstalledVersionInfo,
  private val remoteVersionUseCase: GetRemoteVersionInfo,
  private val versionInfoMapper: VersionInfoMapper
) : BasePresenter<UpdaterView>() {

  private var currentVersionCode = 0

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    fetchCurrentVersionInfo()
  }

  override fun attachView(view: UpdaterView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
    viewState.showEmptyUpdateStatus()
  }

  fun checkForUpdates() {
    remoteVersionUseCase
      .execute()
      .map(versionInfoMapper)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe {
        setConnectionState(ConnectionState.LOADING)
        viewState.showCheckingStatus()
        viewState.setUpdateButtonAvailability(isAvailable = false)
      }
      .doOnSuccess {
        setConnectionState(ConnectionState.COMPLETED)
        viewState.showUpdateCompletedStatus()
      }
      .doOnError {
        setConnectionState(ConnectionState.ERROR)
        viewState.showUpdateErrorStatus()
      }
      .doFinally {
        viewState.setUpdateButtonAvailability(isAvailable = true)
        viewState.showEmptyUpdateStatus()
      }
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ info: AppVersionInfoModel ->

        viewState.displayRemoteVersionInfo(info)

        if (info.versionCode > currentVersionCode) {
          viewState.showUpdateAvailableLabel()
          viewState.setDownloadButtonVisibility(isVisible = true)
        } else {
          viewState.showNoUpdateAvailableLabel()
          viewState.setDownloadButtonVisibility(isVisible = false)
        }

      }, { throwable: Throwable? ->
        throwable?.message?.let { viewState.showErrorMessage(it) }
      })
  }

  fun showChangelog() {
    router.navigateTo(NavigationScreen.CHANGELOG_SCREEN)
  }

  private fun fetchCurrentVersionInfo() {
    installedVersionUseCase
      .execute()
      .map(versionInfoMapper)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ info: AppVersionInfoModel ->
        currentVersionCode = info.versionCode
        viewState.displayInstalledVersionInfo(info)
        viewState.displayLastUpdateCheckDate(info)
      }, { throwable: Throwable? ->
        throwable?.message?.let { viewState.showErrorMessage(it) }
      })
  }
}
