package com.sedsoftware.yaptalker.presentation.feature.updater

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.UpdatesDownloader
import com.sedsoftware.yaptalker.domain.interactor.AppUpdaterInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.mapper.LastUpdateDateMapper
import com.sedsoftware.yaptalker.presentation.mapper.VersionInfoMapper
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class UpdaterPresenter @Inject constructor(
    private val router: Router,
    private val appUpdaterInteractor: AppUpdaterInteractor,
    private val versionInfoMapper: VersionInfoMapper,
    private val dateMapper: LastUpdateDateMapper,
    private val updatesDownloader: UpdatesDownloader,
    private val schedulers: SchedulersProvider
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
        appUpdaterInteractor
            .getRemoteVersionInfo()
            .map(versionInfoMapper)
            .observeOn(schedulers.ui())
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

            }, { e: Throwable? ->
                e?.message?.let { viewState.showErrorMessage(it) }
            })
    }

    fun showChangelog() {
        router.navigateTo(NavigationScreen.CHANGELOG_SCREEN)
    }

    private fun fetchCurrentVersionInfo() {
        appUpdaterInteractor
            .getInstalledVersionInfo()
            .map(versionInfoMapper)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ info: AppVersionInfoModel ->
                currentVersionCode = info.versionCode
                viewState.displayInstalledVersionInfo(info)
            }, { e: Throwable? ->
                e?.message?.let { viewState.showErrorMessage(it) }
            })
    }

    private fun fetchLastUpdateCheckDate() {
        appUpdaterInteractor
            .getLastUpdateCheckDate()
            .map(dateMapper)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ date: String ->
                viewState.displayLastUpdateCheckDate(date)
            }, { e: Throwable? ->
                e?.message?.let { viewState.showErrorMessage(it) }
            })
    }
}
