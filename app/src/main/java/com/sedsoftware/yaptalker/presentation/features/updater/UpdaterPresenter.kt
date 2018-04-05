package com.sedsoftware.yaptalker.presentation.features.updater

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.device.UpdatesDownloader
import com.sedsoftware.yaptalker.domain.interactor.updater.GetInstalledVersionInfo
import com.sedsoftware.yaptalker.domain.interactor.updater.GetLastUpdateDate
import com.sedsoftware.yaptalker.domain.interactor.updater.GetRemoteVersionInfo
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mappers.LastUpdateDateMapper
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
  private val versionInfoMapper: VersionInfoMapper,
  private val getLastUpdateDate: GetLastUpdateDate,
  private val dateMapper: LastUpdateDateMapper,
  private val updatesDownloader: UpdatesDownloader
) : BasePresenter<UpdaterView>() {

  private var currentVersionCode = 0
  private var latestVersionLink = ""

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    fetchCurrentVersionInfo()
    fetchLastUpdateCheckDate()
  }

  override fun attachView(view: UpdaterView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
    viewState.showEmptyUpdateStatus()
  }

  fun downloadNewVersion() {
    latestVersionLink
      .takeIf { it.isNotEmpty() }
      ?.let { link ->
        viewState.setDownloadButtonVisibility(isVisible = false)
        updatesDownloader.initiateUpdateDownloadSession(link)
      }
  }

  fun checkForUpdates() {
    remoteVersionUseCase
      .execute()
      .map(versionInfoMapper)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe {
        viewState.showCheckingStatus()
        viewState.setUpdateButtonAvailability(isAvailable = false)
      }
      .doOnSuccess {
        viewState.showUpdateCompletedStatus()
      }
      .doOnError {
        viewState.showUpdateErrorStatus()
      }
      .doFinally {
        viewState.setUpdateButtonAvailability(isAvailable = true)
        viewState.showEmptyUpdateStatus()
      }
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ info: AppVersionInfoModel ->

        if (info.versionCode > currentVersionCode) {
          viewState.showUpdateAvailableLabel()
          viewState.setDownloadButtonVisibility(isVisible = true)
          viewState.displayRemoteVersionInfo(info)
          latestVersionLink = info.downloadLink
        } else {
          viewState.showNoUpdateAvailableLabel()
          viewState.setDownloadButtonVisibility(isVisible = false)
        }

        fetchLastUpdateCheckDate()

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
      }, { throwable: Throwable? ->
        throwable?.message?.let { viewState.showErrorMessage(it) }
      })
  }

  private fun fetchLastUpdateCheckDate() {
    getLastUpdateDate
      .execute()
      .map(dateMapper)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ date: String ->
        viewState.displayLastUpdateCheckDate(date)
      }, { throwable: Throwable? ->
        throwable?.message?.let { viewState.showErrorMessage(it) }
      })
  }
}